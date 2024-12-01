import { Star } from 'lucide-react';
import { Feedback } from '../hooks/useFeedbacks';

export function renderStars(
	note: number,
	clickable = false,
	selectedNote: number | null = null,
	onClick?: (note: number) => void
) {
	const maxStars = 5;
	const stars = [];

	for (let i = 1; i <= maxStars; i++) {
		stars.push(
			<Star
				key={i}
				className={`h-6 w-6 transition-colors ${
					i <= (clickable ? selectedNote || 0 : note)
						? 'text-yellow-400'
						: 'text-gray-500'
				} ${clickable && 'cursor-pointer'}`}
				onClick={() => clickable && onClick && onClick(i)}
			/>
		);
	}
	return stars;
}

export function getMeanNote(feedbacks: Feedback[]) {
	if (feedbacks.length === 0) return 0;
	const sum = feedbacks.reduce((acc, feedback) => acc + feedback.note, 0);
	return sum / feedbacks.length;
}
