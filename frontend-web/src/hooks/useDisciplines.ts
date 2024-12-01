import { useEffect, useState } from 'react';
import { api } from '../services/api';
import { toast } from 'sonner';
import { UserData } from './useUserData';

export type Discipline = {
	id: string;
	name: string;
	start_time: string;
	end_time: string;
	days_week: string[];
	active: boolean;
};

export function useDisciplines() {
	const [disciplines, setDisciplines] = useState<Discipline[]>();

	useEffect(() => {
		(async () => {
			try {
				const data = await api(`/disciplines/findAll`, {
					method: 'GET',
				});

				if (data) return setDisciplines(data);
			} catch (error) {
				console.error('Error:', error);
				toast.error('Erro ao conectar com o servidor. Verifique sua conexão.');
				return null;
			}
		})();
	}, []);

	return disciplines;
}

export async function getDisciplineListId(userData: UserData | undefined) {
	const disciplinesId: string[] = [];

	if (!userData?.disciplines) return disciplinesId;

	try {
		const disciplinePromises = userData.disciplines.map(
			async (disciplineName) => {
				const data = await api(
					`/disciplines/findByName?name=${disciplineName}`,
					{
						method: 'GET',
					}
				);
				if (data) disciplinesId.push(data[0].id);
			}
		);

		await Promise.all(disciplinePromises);
	} catch (error) {
		console.error('Error:', error);
		toast.error('Erro ao conectar com o servidor. Verifique sua conexão.');
	}

	return disciplinesId;
}

export async function getDisciplineId(disciplineName: string | undefined) {
	if (!disciplineName) return '';

	try {
		const data = await api(`/disciplines/findByName?name=${disciplineName}`, {
			method: 'GET',
		});
		if (data) return data[0].id;
	} catch (error) {
		console.error('Error:', error);
		toast.error('Erro ao conectar com o servidor. Verifique sua conexão.');
	}
}
