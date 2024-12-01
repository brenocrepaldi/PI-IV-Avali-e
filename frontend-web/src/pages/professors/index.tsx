import { PlusIcon, Search } from 'lucide-react';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button } from '../../components/button';
import { Input } from '../../components/input';
import { PageLayout } from '../../components/page-layout';
import { ProfessorList } from './professor-components/professor-list';

export type ExpandedProfessorsState = {
	[key: number]: boolean;
};

export type VisibleFeedbacksState = {
	[key: number]: number;
};

export function ProfessorsPage() {
	const navigate = useNavigate();
	const [expandedProfessors, setExpandedProfessors] =
		useState<ExpandedProfessorsState>({});
	const [visibleFeedbacks, setVisibleFeedbacks] =
		useState<VisibleFeedbacksState>({});
	const [searchTerm, setSearchTerm] = useState('');

	const toggleExpand = (id: number) => {
		setExpandedProfessors((prevState) => {
			const isExpanding = !prevState[id];
			const newExpandedState = {
				...prevState,
				[id]: isExpanding,
			};

			if (!isExpanding) {
				setVisibleFeedbacks((prevVisibleFeedbacks) => ({
					...prevVisibleFeedbacks,
					[id]: 3,
				}));
			}

			return newExpandedState;
		});
	};

	const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		setSearchTerm(e.target.value);
	};

	return (
		<PageLayout title="Professores">
			<div className="bg-zinc-800 p-6 pb-8 rounded-lg shadow-shape text-zinc-300 flex-grow space-y-8">
				<div className="flex justify-between">
					<Button onClick={() => navigate('/professors/add-professor')}>
						<PlusIcon />
						Adicionar Professor
					</Button>

					<Input
						icon={<Search />}
						type="text"
						name="professor"
						placeholder="Buscar professor"
						value={searchTerm}
						onChange={handleSearchChange}
					/>
				</div>

				<div className="h-[1px] rounded-lg bg-zinc-700" />

				<ProfessorList
					expandedProfessors={expandedProfessors}
					toggleExpand={toggleExpand}
					visibleFeedbacks={visibleFeedbacks}
					searchTerm={searchTerm}
				/>
			</div>
		</PageLayout>
	);
}
