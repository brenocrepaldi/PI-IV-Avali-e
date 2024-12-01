import { toast } from 'sonner';
import { api } from '../services/api';
import { Discipline, getDisciplineListId } from './useDisciplines';
import { UserData } from './useUserData';
import { useEffect, useState } from 'react';

export type Feedback = {
	id: string;
	text: string;
	course: string;
	student: string;
	discipline: string;
	note: number;
	date: string;
};

export function useProfessorFeedbacks(professorId: string | null) {
	const [professorFeedbacks, setProfessorFeedbacks] = useState<Feedback[]>();

	useEffect(() => {
		async function fetchProfessorFeedbacks() {
			if (professorId) {
				const data = await getProfessorFeedbacks(professorId);
				if (data) {
					setProfessorFeedbacks(data);
				}
			}
		}
		fetchProfessorFeedbacks();
	}, [professorId]);

	return professorFeedbacks;
}

export async function fetchProfessorFeedbacks(professorId: string) {
	const professorFeedbacks: Feedback[] = await getProfessorFeedbacks(
		professorId
	);
	return professorFeedbacks;
}

// Função para buscar os dados do usuário
async function fetchUserData(userId: string, userType: string) {
	try {
		const response = await api(`/${userType}/findById?id=${userId}`, {
			method: 'GET',
		});
		return response ?? null;
	} catch (error) {
		console.error('Error:', error);
		toast.error('Erro ao conectar com o servidor. Verifique sua conexão.');
		return null;
	}
}

// Função para calcular as avaliações (positivas, neutras, negativas e soma das notas)
function calculateNotes(data: Feedback[]) {
	let positiveNotes = 0;
	let neutralNotes = 0;
	let negativeNotes = 0;
	let sumOfNotes = 0;

	data.forEach((feedback) => {
		sumOfNotes += feedback.note;
		if (feedback.note > 3) {
			positiveNotes++;
		} else if (feedback.note < 3) {
			negativeNotes++;
		} else {
			neutralNotes++;
		}
	});

	return { positiveNotes, neutralNotes, negativeNotes, sumOfNotes };
}

// Função para obter as informações do professor (média e total de avaliações)
export async function getProfessorNoteInfo(professorId: string) {
	const professorFeedbacks = await getProfessorFeedbacks(professorId);

	const totalNotes = professorFeedbacks.length;

	const { positiveNotes, neutralNotes, negativeNotes, sumOfNotes } =
		calculateNotes(professorFeedbacks);

	const meanNote = totalNotes > 0 ? sumOfNotes / totalNotes : 0;

	return {
		meanNote,
		totalNotes,
		positiveNotes,
		neutralNotes,
		negativeNotes,
	};
}

export type DisciplineNote = {
	disciplineName: string;
	positiveNotes: number;
	neutralNotes: number;
	negativeNotes: number;
};

// Função para obter as informações de avaliações por disciplina do professor
export async function getProfessorDisciplineNoteInfo(professorId: string) {
	const professorData = await fetchUserData(professorId, 'professor');
	if (!professorData) return null;
	const professorDisciplinesId = await getDisciplineListId(professorData);

	const disciplineNotes: DisciplineNote[] = [];
	await Promise.all(
		professorDisciplinesId.map(async (disciplineId) => {
			try {
				const data: Feedback[] = await api(
					`/feedback/findByDiscipline?disciplineId=${disciplineId}`,
					{ method: 'GET' }
				);

				if (data && professorData) {
					const { positiveNotes, neutralNotes, negativeNotes } =
						calculateNotes(data);

					const disciplineData: Discipline = await api(
						`/disciplines/findById?id=${disciplineId}`,
						{ method: 'GET' }
					);

					const disciplineName = disciplineData.name;
					disciplineNotes.push({
						disciplineName,
						positiveNotes,
						neutralNotes,
						negativeNotes,
					});
				}
			} catch (error) {
				console.error('Error:', error);
				toast.error('Erro ao conectar com o servidor. Verifique sua conexão.');
			}
		})
	);

	// Retornando as informações de todas as disciplinas
	return disciplineNotes;
}

export async function getProfessorFeedbacks(professorId: string) {
	const professorData: UserData = await fetchUserData(professorId, 'professor');
	if (!professorData) return [];
	const professorDisciplinesId = await getDisciplineListId(professorData);

	const professorFeedbacks: Feedback[] = [];

	await Promise.all(
		professorDisciplinesId.map(async (disciplineId) => {
			try {
				const feedbacks: Feedback[] = await api(
					`/feedback/findByDiscipline?disciplineId=${disciplineId}`,
					{ method: 'GET' }
				);

				if (feedbacks) {
					feedbacks.map((feedback) => {
						professorFeedbacks.push(feedback);
					});
				}
			} catch (error) {
				console.error('Error:', error);
				toast.error('Erro ao conectar com o servidor. Verifique sua conexão.');
			}
		})
	);

	// Ordena os feedbacks em ordem decrescente de data
	professorFeedbacks.sort(
		(a, b) => new Date(b.date).getTime() - new Date(a.date).getTime()
	);

	if (!professorFeedbacks) return [];

	return professorFeedbacks;
}
