import { getNewAccessToken, storeInCache } from './auth';

const apiUrl = import.meta.env.VITE_API_URL;

if (!apiUrl) {
	throw new Error(
		'A URL do backend não foi definida. Verifique a variável de ambiente VITE_API_URL.'
	);
}

export async function api(path: string, options: RequestInit = {}) {
	const accessToken = localStorage.getItem('accessToken');
	if (accessToken) {
		options.headers = {
			...options.headers,
			Authorization: `Bearer ${accessToken}`,
		};
	}

	const response = await fetch(`${apiUrl}${path}`, options);

	if (response.status === 401) {
		if (accessToken) localStorage.removeItem('accessToken');
		if (options.headers)
			delete (options.headers as Record<string, string>)['Authorization'];

		const data = await getNewAccessToken();
		if (data) {
			storeInCache(
				data.id,
				data.accessToken,
				data.access_level,
				data.refreshToken
			);
			console.log('accessToken atualizado com sucesso!');
			window.location.reload(); // revarrega a página para atualizar o token
		}
	}

	if (options.method === 'GET') {
		const data = await response.json();
		return data;
	}

	return response;
}
