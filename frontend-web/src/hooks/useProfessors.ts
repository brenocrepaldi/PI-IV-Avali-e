import { useEffect, useState } from 'react';
import { api } from '../services/api';

export type Professor = {
	id: string;
	name: string;
	ra: string;
	disciplines: string[];
	email: string;
	active: boolean;
};

type ProfessorList = Professor[];

export function useProfessorList() {
	const [professorList, setProfessorList] = useState<ProfessorList | null>(
		null
	);

	useEffect(() => {
		api('/professor/findAll', {
			method: 'GET',
		})
			.then((data: ProfessorList) => {
				setProfessorList(data);
			})
			.catch((error) => {
				console.error('Erro ao buscar a lista de professores:', error);
			});
	}, []);

	return professorList;
}
