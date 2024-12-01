import { toast } from 'sonner';
import { api } from '../services/api';

export async function sendFeedbackRequest(disciplineId: string | undefined) {
	if (!disciplineId) return '';

	try {
		const response = await api(
			`/email/sendFeedback?disciplineId=${disciplineId}`,
			{
				method: 'POST',
			}
		);
		if (response.ok) toast.success('Solicitação enviada!');
	} catch (error) {
		console.error('Error:', error);
		toast.error('Erro ao tentar enviar avaliações.');
	}
}
