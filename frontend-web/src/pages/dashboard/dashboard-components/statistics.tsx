import ReactECharts from 'echarts-for-react';
import { useEffect, useState } from 'react';
import {
	DisciplineNote,
	getProfessorDisciplineNoteInfo,
	getProfessorNoteInfo,
} from '../../../hooks/useFeedbacks';
import { useUserData } from '../../../hooks/useUserData';
import { renderStars } from '../../../utils/feedbackUtils';
import {
	getFeedbackOption,
	getGradesOption,
} from '../../../utils/statisticUtils';

export function Statistics() {
	const userData = useUserData();
	const [noteCategories, setNoteCategories] = useState<{
		positives: number;
		neutral: number;
		negatives: number;
	}>();
	const [totalNote, setTotalNotes] = useState<number>();
	const [meanNote, setMeanNote] = useState<number>(0);
	const [disciplineNotesInfo, setDisciplineNotesInfo] =
		useState<DisciplineNote[]>();

	useEffect(() => {
		async function fetchMeanNote() {
			if (userData) {
				const noteInfo = await getProfessorNoteInfo(userData.id);
				if (noteInfo) {
					setMeanNote(noteInfo.meanNote);
					setNoteCategories({
						positives: noteInfo.positiveNotes,
						neutral: noteInfo.neutralNotes,
						negatives: noteInfo.negativeNotes,
					});
					setTotalNotes(noteInfo.totalNotes);
				}
			}
		}
		async function fetchDisciplinesNotes() {
			if (userData) {
				const disciplineNotes = await getProfessorDisciplineNoteInfo(
					userData.id
				);
				if (disciplineNotes) setDisciplineNotesInfo(disciplineNotes);
			}
		}
		fetchMeanNote();
		fetchDisciplinesNotes();
	}, [userData]);

	return (
		<div className="bg-zinc-800 p-6 rounded-lg shadow-lg text-zinc-300 flex flex-col">
			<h2 className="text-2xl mb-4">Estatísticas de Avaliações</h2>
			<div className="grid grid-cols-1 md:grid-cols-2 gap-4">
				<div className="bg-zinc-700 p-4 pt-8 rounded-md flex flex-col">
					<div className="space-y-6">
						<div className="flex justify-around">
							<div className="border-zinc-600 flex gap-2 items-center">
								<span className="text-lg font-semibold text-zinc-100">
									Média:
								</span>
								<div className="flex items-center">{renderStars(meanNote)}</div>
							</div>
							<div className="border-zinc-600 flex gap-2 items-baseline">
								<span className="text-lg font-semibold text-zinc-200">
									Total de avaliações:
								</span>
								<span className="text-zinc-400 text-lg">{totalNote}</span>
							</div>
						</div>
						<div className="h-[1px] rounded-lg bg-zinc-600" />
						{noteCategories && (
							<ReactECharts
								option={getFeedbackOption(
									noteCategories.positives,
									noteCategories.neutral,
									noteCategories.negatives
								)}
							/>
						)}
					</div>
				</div>

				<div className="bg-zinc-700 p-4 pt-8 rounded-md flex flex-col">
					<div className="text-center font-medium text-xl text-zinc-200 mb-4">
						Avaliações por disciplina
					</div>
					{userData && disciplineNotesInfo && (
						<ReactECharts option={getGradesOption(disciplineNotesInfo)} />
					)}
				</div>
			</div>
		</div>
	);
}
