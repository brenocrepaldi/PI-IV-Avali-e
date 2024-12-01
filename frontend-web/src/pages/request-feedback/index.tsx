import { useState } from 'react';
import { PageLayout } from '../../components/page-layout';
import { useUserData } from '../../hooks/useUserData';
import { Button } from '../../components/button';
import { SendIcon, Loader } from 'lucide-react';
import { getDisciplineId } from '../../hooks/useDisciplines';
import { sendFeedbackRequest } from '../../utils/feedbackRequestUtils';

export function RequestFeedbackPage() {
	const userData = useUserData();
	const [selectedDiscipline, setSelectedDiscipline] = useState<string>();
	const [loading, setLoading] = useState(false);

	async function sendRequest() {
		setLoading(true);
		try {
			const selectedDisciplineId = await getDisciplineId(selectedDiscipline);
			await sendFeedbackRequest(selectedDisciplineId);
		} catch (error) {
			console.error('Erro ao enviar solicitação:', error);
		} finally {
			setLoading(false);
		}
	}

	return (
		<PageLayout title="Solicitar Avaliação">
			<div className="flex-grow h-full max-h-screen bg-zinc-800 p-10 rounded-xl shadow-xl text-zinc-100">
				<div className="flex flex-col items-center justify-center space-y-8 p-10 rounded-md overflow-hidden">
					<h2 className="text-5xl font-bold text-center text-white mb-4">
						Solicite sua Avaliação
					</h2>
					<p className="text-center text-zinc-300 mb-8 max-w-xl">
						Selecione a disciplina e envie uma solicitação de avaliação. O
						sistema automaticamente gerenciará as solicitações para cada aluno
						da disciplina selecionada.
					</p>

					<div className="w-1/2 flex items-center justify-center flex-col gap-6">
						<div className="w-full">
							<label
								htmlFor="course"
								className="block text-xl font-medium text-zinc-200 mb-2"
							>
								Selecione a Disciplina
							</label>
							<select
								id="course"
								name="course"
								value={selectedDiscipline ?? ''}
								onChange={(e) => setSelectedDiscipline(e.target.value)}
								className="bg-zinc-600 text-zinc-100 p-4 py-3 rounded-md w-full focus:outline-none focus:ring-2 focus:ring-zinc-500  shadow-lg"
							>
								<option className="text-center text-zinc-400" value="" disabled>
									Selecione uma disciplina
								</option>
								{userData?.disciplines?.map((discipline, index) => (
									<option
										className="text-center"
										key={index}
										value={discipline}
									>
										{discipline}
									</option>
								))}
							</select>
						</div>

						<div className="items-center justify-center">
							<Button
								type="button"
								disabled={!selectedDiscipline || loading}
								variant={`${!selectedDiscipline ? 'secondary' : 'primary'}`}
								onClick={sendRequest}
							>
								{loading ? (
									<Loader className="mr-3 animate-spin" />
								) : (
									<SendIcon className="mr-3" />
								)}
								{loading
									? 'Enviando Solicitação...'
									: `${
											!selectedDiscipline
												? 'Selecione uma disciplina'
												: 'Enviar solicitação'
										}`}
							</Button>
						</div>
					</div>
				</div>
			</div>
		</PageLayout>
	);
}
