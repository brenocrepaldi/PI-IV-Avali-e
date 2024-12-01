import { PlusIcon } from 'lucide-react';
import { useState } from 'react';
import { toast } from 'sonner';
import { Button } from '../../../components/button';
import { Input } from '../../../components/input';
import {
	Discipline,
	getDisciplineListId,
	useDisciplines,
} from '../../../hooks/useDisciplines';
import { UserData } from '../../../hooks/useUserData';
import { addNewProfessor } from '../../../utils/professorUtils';

export function ProfessorForm() {
	const [newProfessorFormData, setNewProfessorFormData] = useState<UserData>();
	const [showPassword, setShowPassword] = useState(false);
	const disciplines = useDisciplines();

	const handleInputChange = (
		e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
	) => {
		const { name, value } = e.target;
		setNewProfessorFormData((prevData) => ({
			...(prevData as UserData),
			[name]: value,
		}));
	};

	const handleCheckboxChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		const { name, checked } = e.target;
		setNewProfessorFormData((prevData) => ({
			...(prevData as UserData),
			[name]: checked,
		}));
	};

	const handlePasswordVisibility = () => setShowPassword(!showPassword);

	const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
		e.preventDefault();
		if (!newProfessorFormData) return;

		const disciplinesId = await getDisciplineListId(newProfessorFormData);
		newProfessorFormData.disciplines = disciplinesId;

		const newProfessorData = {
			name: newProfessorFormData.name,
			password: newProfessorFormData.password,
			email: newProfessorFormData.email,
			ra: newProfessorFormData.ra,
			disciplines: newProfessorFormData.disciplines,
			active: newProfessorFormData.active,
		};

		const professorAdded = await addNewProfessor(newProfessorData);

		if (professorAdded) toast.success(`Professor cadastrado com sucesso!`);
		else toast.error(`Erro ao cadastrar o professor!`);
	};

	const handleToggleDiscipline = (discipline: Discipline) => {
		setNewProfessorFormData((prevData) => {
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

	return (
		<form onSubmit={handleSubmit} className="space-y-6">
			<div className="space-y-4">
				<Input
					label="RA"
					type="number"
					id="ra"
					name="ra"
					value={newProfessorFormData?.ra || ''}
					onChange={handleInputChange}
					placeholder="Insira o RA"
					required
				/>
				<Input
					label="Nome completo"
					type="text"
					id="name"
					name="name"
					value={newProfessorFormData?.name || ''}
					onChange={handleInputChange}
					placeholder="Insira o nome completo"
					required
				/>
				<Input
					label="E-mail"
					type="email"
					id="email"
					name="email"
					value={newProfessorFormData?.email || ''}
					onChange={handleInputChange}
					placeholder="Insira o e-mail"
					required
				/>
				<Input
					label="Senha"
					isPassword={true}
					handlePasswordVisibility={handlePasswordVisibility}
					showPassword={showPassword}
					type={showPassword ? 'text' : 'password'}
					name="password"
					placeholder="Digite sua senha"
					value={newProfessorFormData?.password || ''}
					onChange={handleInputChange}
					required
				/>
				<div>
					<span className="text-zinc-300">Disciplinas</span>
					<div className="flex flex-wrap gap-2 mt-2">
						{disciplines &&
							disciplines.map((discipline) => (
								<button
									key={discipline.id}
									type="button"
									className={`border-2 rounded-md py-1 px-2 text-sm font-medium transition duration-200 ${
										newProfessorFormData?.disciplines?.includes(discipline.name)
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
						Selecionadas: {newProfessorFormData?.disciplines?.join(', ')}
					</p>
				</div>
				<div className="flex items-center justify-start gap-2">
					<input
						type="checkbox"
						id="active"
						name="active"
						checked={newProfessorFormData?.active || false}
						onChange={handleCheckboxChange}
						className="w-4 h-4"
					/>
					<label htmlFor="active" className="text-zinc-200">
						Ativo
					</label>
				</div>
			</div>
			<div className="flex justify-end">
				<Button type="submit">
					<PlusIcon />
					Adicionar Professor
				</Button>
			</div>
		</form>
	);
}
