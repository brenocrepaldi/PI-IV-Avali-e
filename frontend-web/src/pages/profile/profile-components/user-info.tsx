import { Check, Pencil } from 'lucide-react';
import { useEffect, useState } from 'react';
import { Button } from '../../../components/button';
import { Input } from '../../../components/input';
import { getCourseId, useCourses } from '../../../hooks/useCourses';
import {
	Discipline,
	getDisciplineListId,
	useDisciplines,
} from '../../../hooks/useDisciplines';
import { useUserAccessLevel } from '../../../hooks/useUserAccessLevel';
import { UserData, useUserData } from '../../../hooks/useUserData';
import { NewUserData } from '../../../services/auth';

interface UserInfoProps {
	isEditing: boolean;
	toggleEdit: () => void;
	toggleConfirmationModal: (userData: NewUserData) => void;
}

export function UserInfo({
	isEditing,
	toggleEdit,
	toggleConfirmationModal,
}: UserInfoProps) {
	const userAccessLevel = useUserAccessLevel();
	const userData = useUserData();
	const disciplines = useDisciplines();
	const courses = useCourses();

	const [userFormData, setUserFormData] = useState<UserData | undefined>();

	useEffect(() => {
		if (userData) {
			setUserFormData(userData);
		}
	}, [userData]);

	const handleInputChange = (
		e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
	) => {
		const { name, value } = e.target;
		setUserFormData((prevData) => ({
			...(prevData as UserData),
			[name]: value,
		}));
	};

	const handleToggleDiscipline = (discipline: Discipline) => {
		setUserFormData((prevData) => {
			const currentDisciplines = prevData?.disciplines || [];
			const updatedDisciplines = currentDisciplines.includes(discipline.name)
				? currentDisciplines.filter((d) => d !== discipline.name)
				: [...currentDisciplines, discipline.name];

			return {
				...(prevData as UserData),
				disciplines: updatedDisciplines,
			};
		});
	};

	const handleCancel = () => {
		setUserFormData(userData);
		toggleEdit();
	};

	const handleSave = async () => {
		if (!userFormData) return;

		if (userAccessLevel === 1) {
			const disciplinesId = await getDisciplineListId(userFormData);
			userFormData.disciplines = disciplinesId;
		} else {
			const courseId = await getCourseId(userFormData);
			userFormData.course = courseId;
		}

		const newUserData =
			userAccessLevel === 1
				? {
						ra: userFormData.ra,
						name: userFormData.name,
						email: userFormData.email,
						disciplines: userFormData.disciplines,
					}
				: {
						ra: userFormData.ra,
						name: userFormData.name,
						email: userFormData.email,
						course: userFormData.course,
					};

		toggleConfirmationModal({ id: userFormData.id, ...newUserData });
	};

	return (
		<div className="space-y-3 p-2">
			<h2 className="text-2xl ml-[-10px]">Informações</h2>
			{!isEditing ? (
				<div className="bg-zinc-900 p-8 rounded-2xl shadow-xl">
					<div>
						<p className="text-sm text-gray-500 italic">
							{userAccessLevel === 1 ? 'Professor' : 'Diretor'}
						</p>
						<div className="flex items-center justify-between mt-4">
							<h2 className="text-3xl font-bold text-white">
								{userData?.name}
							</h2>
							<button
								className="text-gray-400 hover:text-white transition duration-200 ease-in-out transform hover:scale-105"
								onClick={toggleEdit}
							>
								<Pencil size={22} />
							</button>
						</div>
						<p className="text-sm text-gray-400">{userData?.email}</p>
					</div>
					<div className="h-[1px] rounded-lg bg-zinc-700 my-4" />
					<div className="mt-6 space-y-2">
						<h3 className="text-xl font-semibold text-gray-300">
							Informações Adicionais
						</h3>
						<ul className="list-inside text-gray-400 pl-2 space-y-2 list-none">
							<li>
								RA:{' '}
								<span className="font-medium text-white">{userData?.ra}</span>
							</li>
							{userAccessLevel === 2 && (
								<li>
									Curso:{' '}
									<span className="font-medium text-white">
										{userData?.course}
									</span>
								</li>
							)}
							{userAccessLevel === 1 &&
								userData?.disciplines &&
								userData.disciplines.length > 0 && (
									<li>
										Disciplinas:{' '}
										<div className="flex flex-wrap gap-2 mt-2">
											{userData.disciplines.map((discipline, index) => (
												<span
													key={index}
													className="bg-sky-600 text-white px-4 py-2 rounded-full text-sm font-medium shadow-lg"
												>
													{discipline}{' '}
												</span>
											))}
										</div>
									</li>
								)}
						</ul>
					</div>
				</div>
			) : (
				<form className="space-y-6 py-4">
					<div className="space-y-4">
						<Input
							label="Nome completo"
							type="text"
							id="name"
							name="name"
							value={userFormData?.name}
							onChange={handleInputChange}
							placeholder="Insira o nome completo"
							required
						/>
						<Input
							label="E-mail"
							type="email"
							id="email"
							name="email"
							value={userFormData?.email}
							onChange={handleInputChange}
							placeholder="Insira o e-mail"
							required
						/>
						<Input
							label="RA"
							type="number"
							id="ra"
							name="ra"
							value={userFormData?.ra}
							onChange={handleInputChange}
							placeholder="Insira o RA"
							required
						/>
						{userAccessLevel === 1 && (
							<div>
								<span className="text-zinc-300">Disciplinas</span>
								<div className="flex flex-wrap gap-2 mt-2">
									{disciplines &&
										disciplines.map((discipline) => (
											<button
												key={discipline.id}
												type="button"
												className={`border-2 rounded-md py-1 px-2 text-sm font-medium transition duration-200 ${
													userFormData?.disciplines?.includes(discipline.name)
														? 'bg-sky-600 border-sky-400 text-white'
														: 'bg-gray-700 border-gray-600 text-gray-300 hover:bg-gray-600'
												}`}
												onClick={() => handleToggleDiscipline(discipline)}
											>
												{discipline.name}
											</button>
										))}
								</div>
								<p className="mt-2 text-sm text-gray-400">
									Selecionadas: {userFormData?.disciplines?.join(', ')}
								</p>
							</div>
						)}
						{userAccessLevel === 2 && (
							<div className="flex flex-col">
								<label htmlFor="course" className="text-zinc-300">
									Curso
								</label>
								<select
									id="course"
									name="course"
									value={userFormData?.course}
									onChange={handleInputChange}
									className="bg-zinc-900 text-zinc-200 p-2 rounded-md w-full h-12 mt-2"
								>
									<option value="" disabled>
										Selecione um curso
									</option>
									{courses &&
										courses.map((course) => (
											<option key={course.id} value={course.name}>
												{course.name}
											</option>
										))}
								</select>
							</div>
						)}
					</div>
					<div className="flex justify-end gap-4">
						<Button type="button" variant="secondary" onClick={handleCancel}>
							Cancelar
						</Button>
						<Button type="button" onClick={handleSave}>
							<Check />
							Salvar
						</Button>
					</div>
				</form>
			)}
		</div>
	);
}
