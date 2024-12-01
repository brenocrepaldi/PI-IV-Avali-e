import { renderStars } from '../../../utils/feedbackUtils';

interface FeedbackItemProps {
	note: number;
	comment: string;
	date: string;
}

export function FeedbackItem({ note: note, comment, date }: FeedbackItemProps) {
	return (
		<div className="w-full p-4 bg-zinc-700 rounded-lg shadow-md flex items-start gap-4 transition-transform hover:bg-zinc-600">
			<div className="flex items-center">{renderStars(note)}</div>
			<div className="flex flex-col gap-1">
				<span className="text-zinc-100 italic">{comment}</span>
				<span className="text-zinc-500 text-sm">
					{new Date(date).toLocaleDateString('pt-BR')} às{' '}
					{new Date(date).toLocaleTimeString('pt-BR', {
						hour: '2-digit',
						minute: '2-digit',
					})}
				</span>
			</div>
		</div>
	);
}
