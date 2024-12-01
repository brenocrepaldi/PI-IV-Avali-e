import { useEffect, useState } from 'react';
import { api } from '../services/api';
import { toast } from 'sonner';
import { UserData } from './useUserData';

export type Course = {
	id: string;
	name: string;
	active: boolean;
};

export function useCourses() {
	const [courses, setCourses] = useState<Course[]>();

	useEffect(() => {
		(async () => {
			try {
				const data = await api(`/course/findAll`, {
					method: 'GET',
				});

				if (data) return setCourses(data);
			} catch (error) {
				console.error('Error:', error);
				toast.error('Erro ao conectar com o servidor. Verifique sua conexão.');
				return null;
			}
		})();
	}, []);

	return courses;
}

export async function getCourseId(userData: UserData | undefined) {
	let courseId = '';
	if (!userData?.course) return courseId;

	try {
		const courseData: Course[] = await api(
			`/course/findByName?name=${userData.course}`,
			{
				method: 'GET',
			}
		);

		if (courseData) courseId = courseData[0].id;
	} catch (error) {
		console.error('Error:', error);
		toast.error('Erro ao conectar com o servidor. Verifique sua conexão.');
	}

	return courseId;
}
