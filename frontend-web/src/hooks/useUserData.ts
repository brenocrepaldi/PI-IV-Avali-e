import { useEffect, useState } from 'react';
import { useUserId } from './useUserId';
import { useUserAccessLevel } from './useUserAccessLevel';
import { api } from '../services/api';
import { toast } from 'sonner';

export type UserData = {
	id: string;
	ra: string;
	name: string;
	email: string;
	password: string;
	disciplines?: string[];
	course?: string;
	active: boolean;
};

export function useUserData() {
	const userId = useUserId();
	const userAccessLevel = useUserAccessLevel();
	const [userData, setUserData] = useState<UserData>();

	useEffect(() => {
		const userType = userAccessLevel === 1 ? 'professor' : 'director';
		if (userId && userAccessLevel !== null) {
			(async () => {
				try {
					const data = await api(`/${userType}/findById?id=${userId}`, {
						method: 'GET',
					});

					if (data) return setUserData(data);
				} catch (error) {
					console.error('Error:', error);
					toast.error(
						'Erro ao conectar com o servidor. Verifique sua conexão.'
					);
					return null;
				}
			})();
		}
	}, [userId, userAccessLevel]);

	return userData;
}
