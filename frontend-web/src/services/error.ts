import { toast } from 'sonner';

export function handleErrorResponse(response: Response) {
	switch (response.status) {
		case 400:
			toast.error('Requisição inválida. Verifique os dados enviados.');
			break;
		case 401:
			toast.error('Credenciais incorretas. Verifique seu e-mail e senha.');
			break;
		case 403:
			toast.error(
				'Acesso negado. Você não tem permissão para realizar esta ação.'
			);
			break;
		case 404:
			toast.error(
				'Recurso não encontrado. Verifique o endereço ou tente novamente mais tarde.'
			);
			break;
		case 429:
			toast.error('Muitas requisições. Aguarde um momento e tente novamente.');
			break;
		case 500:
			toast.error('Erro interno no servidor. Tente novamente mais tarde.');
			break;
		case 503:
			toast.error('Serviço indisponível. O servidor pode estar em manutenção.');
			break;
		default:
			toast.error(
				`Erro inesperado (código ${response.status}). Tente novamente.`
			);
	}
}

// Tratamento de exceções fora da requisição, como erros de rede
export function handleNetworkError(error: unknown) {
	if (error instanceof TypeError) {
		toast.error('Falha de conexão. Verifique sua internet e tente novamente.');
	} else {
		toast.error('Erro desconhecido. Tente novamente.');
	}
}
