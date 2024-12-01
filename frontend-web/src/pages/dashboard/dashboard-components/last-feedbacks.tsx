import { useNavigate } from 'react-router-dom';
import { Button } from '../../../components/button';
import { renderStars } from '../../../utils/feedbackUtils';
import { useProfessorFeedbacks } from '../../../hooks/useFeedbacks';
import { useUserData } from '../../../hooks/useUserData';

export function LastFeedbacks() {
	const navigate = useNavigate();
	const userData = useUserData();
	const professorFeedbacks = useProfessorFeedbacks(
		userData ? userData.id : null
	);

	return (
		<div className="bg-zinc-800 p-6 pb-8 rounded-lg shadow-shape text-zinc-300 flex-grow">
			<h2 className="text-2xl mb-4">Últimas Avaliações</h2>

			<div className="flex flex-col gap-4">
				<div className="flex flex-col gap-4">
					{professorFeedbacks &&
						professorFeedbacks.slice(0, 3).map((feedback, index) => (
							<div
								key={index}
								className="w-full p-4 py-6 bg-zinc-700 hover:bg-zinc-600 rounded-md flex items-center gap-3 transition-all duration-100"
							>
								<span className="text-zinc-100 flex">
									{renderStars(feedback.note)}
								</span>
								<span className="text-zinc-300 italic">"{feedback.text}"</span>
								<span className="text-zinc-400 text-sm ml-auto">
									{new Date(feedback.date).toLocaleDateString('pt-BR')} às{' '}
									{new Date(feedback.date).toLocaleTimeString('pt-BR', {
										hour: '2-digit',
										minute: '2-digit',
									})}
								</span>
							</div>
						))}

					<Button
						size="full"
						onClick={() => {
							navigate('/feedbacks');
						}}
					>
						Ver todas as avaliações
					</Button>
				</div>
			</div>
		</div>
	);
}
