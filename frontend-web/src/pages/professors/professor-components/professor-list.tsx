import { useState } from 'react';
import { ChevronDown, ChevronUp } from 'lucide-react';
import { Button } from '../../../components/button';
import { ExpandedProfessorsState, VisibleFeedbacksState } from '../index';
import { Feedback, fetchProfessorFeedbacks } from '../../../hooks/useFeedbacks';
import { useNavigate } from 'react-router-dom';
import { getMeanNote, renderStars } from '../../../utils/feedbackUtils';
import { Professor, useProfessorList } from '../../../hooks/useProfessors';

interface ProfessorListProps {
	expandedProfessors: ExpandedProfessorsState;
	toggleExpand: (id: number) => void;
	visibleFeedbacks: VisibleFeedbacksState;
	searchTerm: string;
}

export function ProfessorList({
	expandedProfessors,
	toggleExpand,
	visibleFeedbacks,
	searchTerm,
}: ProfessorListProps) {
	const navigate = useNavigate();
	const [professorFeedbacks, setProfessorFeedbacks] = useState<{
		[key: string]: Feedback[];
	}>({});
	const professorList = useProfessorList();

	const loadFeedbacks = async (professorId: string) => {
		if (!professorFeedbacks[professorId]) {
			const professorFeedbacks = await fetchProfessorFeedbacks(professorId);
			setProfessorFeedbacks((prev) => ({
				...prev,
				[professorId]: professorFeedbacks || [],
			}));
		}
	};

	if (!professorList) return null;

	const filteredProfessors = professorList.filter((professor) =>
		professor.name.toLowerCase().includes(searchTerm.toLowerCase())
	);

	return (
		<div className="space-y-6">
			<h2 className="text-2xl">Lista de Professores</h2>
			<div className="px-2 rounded-md flex flex-col space-y-4">
				{professorList &&
					filteredProfessors.map((professor: Professor, index: number) => (
						<div
							key={index}
							className={`p-4 bg-zinc-700 rounded-lg transition-all duration-100 ease-in-out space-y-6 ${
								!expandedProfessors[index] && 'hover:bg-zinc-600 cursor-pointer'
							}`}
							onClick={() => {
								toggleExpand(index);
								loadFeedbacks(professor.id);
							}}
						>
							<div className="flex items-center justify-between">
								<h2 className="text-xl font-bold cursor-pointer text-zinc-300">
									{professor.name}
								</h2>
								{expandedProfessors[index] ? (
									<ChevronUp className="cursor-pointer" />
								) : (
									<ChevronDown className="cursor-pointer" />
								)}
							</div>
							{expandedProfessors[index] && (
								<div
									className="transition-all duration-500 ease-in-out overflow-hidden flex flex-col gap-6 p-6 bg-zinc-800 rounded-lg shadow-md"
									onClick={(e) => e.stopPropagation()}
								>
									<div className="border-zinc-600 flex gap-2 items-baseline">
										<span className="text-lg font-semibold text-zinc-100">
											RA:
										</span>
										<span className="text-zinc-400">{professor.ra}</span>
									</div>

									<div className="h-[1px] rounded-lg bg-zinc-700" />

									<div className="border-zinc-600 flex gap-2 items-baseline">
										<span className="text-lg font-semibold text-zinc-100">
											E-mail:
										</span>
										<span className="text-zinc-400">{professor.email}</span>
									</div>

									<div className="h-[1px] rounded-lg bg-zinc-700" />

									<div className="border-zinc-600 flex gap-2 items-center">
										<span className="text-lg font-semibold text-zinc-100">
											Média de Avaliação:
										</span>
										{professorFeedbacks[professor.id] && (
											<div className="flex items-center">
												{renderStars(
													getMeanNote(professorFeedbacks[professor.id])
												)}
											</div>
										)}
									</div>

									<div className="h-[1px] rounded-lg bg-zinc-700" />

									<div className="border-zinc-600 flex gap-2 items-baseline">
										<span className="text-lg font-semibold text-zinc-100">
											Disciplinas:
										</span>
										<span className="text-zinc-400">
											<div className="flex flex-wrap gap-2 mt-2">
												{professor.disciplines.map((discipline, index) => (
													<span
														key={index}
														className="bg-sky-400 text-zinc-800 px-4 py-2 rounded-full text-sm font-medium"
													>
														{discipline}
													</span>
												))}
											</div>
										</span>
									</div>

									<div className="h-[1px] rounded-lg bg-zinc-700" />

									<div className="space-y-4">
										<h3 className="text-lg font-semibold text-zinc-100">
											Últimas Avaliações:
										</h3>
										<ul className="space-y-3">
											{(professorFeedbacks[professor.id] || [])
												.slice(0, visibleFeedbacks[index] || 3)
												.map((feedback, index) => (
													<li key={index} className="flex rounded-lg">
														<div className="w-full px-4 py-4 bg-zinc-700 rounded-xl shadow-shape flex items-center gap-3">
															<span className="text-zinc-100 flex">
																{renderStars(feedback.note)}
															</span>
															<span className="text-zinc-300 italic">
																"{feedback.text}"
															</span>
															<span className="text-zinc-400 text-sm ml-auto">
																{new Date(feedback.date).toLocaleDateString(
																	'pt-BR'
																)}{' '}
																às{' '}
																{new Date(feedback.date).toLocaleTimeString(
																	'pt-BR',
																	{
																		hour: '2-digit',
																		minute: '2-digit',
																	}
																)}
															</span>
														</div>
													</li>
												))}
										</ul>
										{(professorFeedbacks[professor.id]?.length || 0) >
											(visibleFeedbacks[index] || 3) && (
											<Button
												size="full"
												className="text-zinc-400 hover:text-zinc-200 underline mt-2"
												onClick={() => {
													navigate('/feedbacks', {
														state: { professorId: professor.id },
													});
												}}
											>
												Ver todas as avaliações
											</Button>
										)}
									</div>
								</div>
							)}
						</div>
					))}
			</div>
		</div>
	);
}
