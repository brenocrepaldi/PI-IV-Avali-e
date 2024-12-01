import { Feedback } from '../../../hooks/useFeedbacks';
import { getMeanNote, renderStars } from '../../../utils/feedbackUtils';

interface FeedbackSummaryProps {
	totalFeedbacks: number;
	feedbacks: Feedback[];
}

export function FeedbackSummary({
	totalFeedbacks: totalFeedback,
	feedbacks,
}: FeedbackSummaryProps) {
	return (
		<div className="h-16 flex-1 p-3 rounded-md bg-zinc-700 flex justify-around items-center transition-all duration-100">
			<div className="flex items-center gap-2">
				<span className="text-xl font-semibold text-zinc-200">
					Total de avaliações:
				</span>
				<span className="text-zinc-400 text-xl">{totalFeedback}</span>
			</div>
			<div className="flex items-center gap-2">
				<span className="text-xl font-semibold text-zinc-100">
					Média das avaliações:
				</span>
				<div className="flex">{renderStars(getMeanNote(feedbacks))}</div>
			</div>
		</div>
	);
}
