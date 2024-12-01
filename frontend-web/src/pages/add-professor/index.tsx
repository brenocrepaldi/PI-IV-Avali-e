import { useNavigate } from 'react-router-dom';
import { PageLayout } from '../../components/page-layout';
import { BackButton } from './add-professor-components/back-button';
import { ProfessorForm } from './add-professor-components/professor-form';

export function AddProfessorPage() {
	const navigate = useNavigate();

	return (
		<PageLayout title="Professores">
			<div className="flex gap-2">
				<BackButton onClick={() => navigate('/professors')} />
				<div className="w-full bg-zinc-800 rounded-xl shadow-lg">
					<div className="w-1/3 p-6 flex-grow space-y-6">
						<h2 className="text-2xl">Adicionar Professor</h2>
						<div className="h-[1px] rounded-lg bg-zinc-700" />
						<ProfessorForm />
					</div>
				</div>
			</div>
		</PageLayout>
	);
}
