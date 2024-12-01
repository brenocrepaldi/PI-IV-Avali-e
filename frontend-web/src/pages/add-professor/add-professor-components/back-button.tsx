import { ChevronLeft } from 'lucide-react';

interface BackButtonProps {
	onClick: () => void;
}

export function BackButton({ onClick }: BackButtonProps) {
	return (
		<div
			className="flex items-center justify-center flex-shrink-0 bg-zinc-700 hover:bg-zinc-600 transition-all duration-100 p-4 rounded-lg shadow-lg text-zinc-400 cursor-pointer"
			onClick={onClick}
		>
			<ChevronLeft />
		</div>
	);
}
