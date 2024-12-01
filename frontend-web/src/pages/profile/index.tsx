import { useState } from 'react';
import { PageLayout } from '../../components/page-layout';
import { ConfirmationModal } from './profile-components/confirmation-modal';
import { PasswordChange } from './profile-components/password-change';
import { UserInfo } from './profile-components/user-info';
import { NewUserData, updateUserData } from '../../services/auth';
import { toast } from 'sonner';

export function ProfilePage() {
	const [isEditing, setIsEditing] = useState(false);
	const [isConfirmationModalOpen, setIsConfirmationModalOpen] = useState(false);
	const [pendingUserData, setPendingUserData] = useState<NewUserData>();

	const toggleEdit = () => setIsEditing((prev) => !prev);
	const toggleConfirmationModal = () =>
		setIsConfirmationModalOpen((prev) => !prev);

	const handleUpdateUserData = async (userData: NewUserData | undefined) => {
		if (
			userData &&
			userData.id &&
			(await updateUserData(userData.id, userData))
		) {
			toggleEdit();
			toast.success('Dados atualizados com sucesso!');
		}
	};

	return (
		<PageLayout title="Perfil">
			<div className="bg-zinc-800 p-6 pb-8 pt-2 rounded-lg shadow-shape text-zinc-300 flex-grow">
				<div className="w-1/3 py-6 px-3 rounded-md flex flex-col space-y-6">
					<UserInfo
						isEditing={isEditing}
						toggleEdit={toggleEdit}
						toggleConfirmationModal={(userData) => {
							setPendingUserData(userData);
							toggleConfirmationModal();
						}}
					/>
					<div className="h-[1px] rounded-lg bg-zinc-700" />
					<PasswordChange />
				</div>
			</div>

			{isConfirmationModalOpen && (
				<ConfirmationModal
					confirm={() => handleUpdateUserData(pendingUserData)}
					toggleModal={toggleConfirmationModal}
				/>
			)}
		</PageLayout>
	);
}
