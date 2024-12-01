import { useState } from 'react';
import { PageLayout } from '../../components/page-layout';
import { SlidersVertical } from 'lucide-react';
import { FilterPanel } from './feedback-components/filter-panel';
import { FeedbackSummary } from './feedback-components/feedback-summary';
import { FeedbackItem } from './feedback-components/feedback-item';
import { useUserData } from '../../hooks/useUserData';
import { useProfessorFeedbacks } from '../../hooks/useFeedbacks';
import { useLocation } from 'react-router-dom';

export function FeedbacksPage() {
	const location = useLocation();
	const professorId = location.state?.professorId;
	const userData = useUserData();

	const professorFeedbacks = useProfessorFeedbacks(
		professorId ?? (userData ? userData.id : null)
	);
	const [selectedNote, setSelectedNote] = useState<number | null>(null);
	const [filterOpen, setFilterOpen] = useState(false);

	if (!userData && !professorFeedbacks) return null;

	const filteredFeedbacks =
		!filterOpen || selectedNote === null
			? professorFeedbacks
			: professorFeedbacks &&
				professorFeedbacks.filter((feedback) => feedback.note === selectedNote);

	return (
		<PageLayout title="Dashboard">
			<div className="bg-zinc-800 p-6 pb-8 rounded-lg shadow-md">
				<div className="space-y-5">
					<div className="flex justify-between items-center mb-6">
						<h2 className="text-2xl text-zinc-200">Todas avaliações</h2>
					</div>

					<div className="h-[1px] rounded-lg bg-zinc-700" />

					<div className="w-full flex items-center justify-center gap-2">
						{filterOpen ? (
							<FilterPanel
								selectedNote={selectedNote}
								setSelectedNote={setSelectedNote}
							/>
						) : (
							professorFeedbacks && (
								<FeedbackSummary
									totalFeedbacks={professorFeedbacks.length}
									feedbacks={professorFeedbacks}
								/>
							)
						)}
						<button
							onClick={() => {
								setFilterOpen(!filterOpen);
								setSelectedNote(null);
							}}
							className={`h-[64px] w-[52px] flex items-center justify-center p-2 rounded-lg text-zinc-100 transition-colors ${
								filterOpen && 'bg-zinc-700 hover:bg-zinc-600'
							}`}
							aria-label="Toggle Filter"
						>
							<SlidersVertical />
						</button>
					</div>

					<div className="min-h-40 flex flex-col gap-4 p-10 px-20 bg-zinc-900 rounded-lg items-center justify-center">
						{filteredFeedbacks && filteredFeedbacks.length > 0 ? (
							filteredFeedbacks.map((feedback, index) => (
								<FeedbackItem
									key={index}
									note={feedback.note}
									comment={feedback.text}
									date={feedback.date}
								/>
							))
						) : (
							<div className="text-zinc-400 text-center">
								Nenhuma avaliação encontrada.
							</div>
						)}
					</div>
				</div>
			</div>
		</PageLayout>
	);
}
