import { useState } from 'react';
import { toast } from 'sonner';
import { LockOpenIcon } from 'lucide-react';
import { Button } from '../../../components/button';
import { Input } from '../../../components/input';

export function PasswordChange() {
	const [isEditing, setIsEditing] = useState(false);
	const [password, setPassword] = useState('');
	const [showPassword, setShowPassword] = useState(false);

	const handleEditToggle = () => setIsEditing((prev) => !prev);
	const handlePasswordVisibility = () => setShowPassword((prev) => !prev);

	const handleSubmit = () => {
		// Lógica para atualizar senha
		console.log('Nova senha: ', password);
		handleEditToggle();
		toast.success('Senha alterada!');
	};

	return (
		<div className="space-y-4">
			<h2 className="text-2xl ml-[-10px]">Segurança</h2>
			{!isEditing ? (
				<Button
					type="button"
					variant="secondary"
					size="full"
					onClick={handleEditToggle}
				>
					<LockOpenIcon size={18} />
					Alterar Senha
				</Button>
			) : (
				<div className="space-y-3">
					<div>
						<Input
							label="Nova senha"
							isPassword
							handlePasswordVisibility={handlePasswordVisibility}
							showPassword={showPassword}
							type={showPassword ? 'text' : 'password'}
							placeholder="Insira uma nova senha"
							onChange={(e) => setPassword(e.target.value)}
							required
						/>
					</div>
					<div className="flex justify-end gap-2">
						<Button
							type="button"
							variant="secondary"
							onClick={handleEditToggle}
						>
							Cancelar
						</Button>
						<Button type="button" onClick={handleSubmit}>
							Confirmar
						</Button>
					</div>
				</div>
			)}
		</div>
	);
}
