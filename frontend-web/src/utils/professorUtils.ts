import { toast } from 'sonner';
import { api } from '../services/api';
import { handleErrorResponse } from '../services/error';

export type NewProfessor = {
	ra: string;
	name: string;
	email: string;
	password: string;
	disciplines: string[];
	active: boolean;
};

export async function addNewProfessor(professorData: NewProfessor) {
	try {
		const response = await api('/professor/register', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify(professorData),
		});

		if (response.ok) {
			return true;
		} else handleErrorResponse(response);
	} catch (error) {
		console.error('Error:', error);
		toast.error('Erro ao conectar com o servidor. Verifique sua conexão.');
	}
}
