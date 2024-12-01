import { AtSign, X } from 'lucide-react';
import { Button } from '../../../components/button';
import { toast } from 'sonner';
import { Input } from '../../../components/input';
import { useState } from 'react';

interface ForgotPasswordModalProps {
	handleForgotPasswordModal: () => void;
}

export function ForgotPasswordModal({
	handleForgotPasswordModal,
}: ForgotPasswordModalProps) {
	const [email, setEmail] = useState('');
	function sendEmail() {
		// Lógica para envio de e-mail
		handleForgotPasswordModal();
		toast.success('E-mail enviado!');
	}
	return (
		<div className="fixed inset-0 bg-black/60 flex items-center justify-center">
			<div className="w-[640px] rounded-xl py-5 px-6 shadow-shape bg-zinc-800 space-y-5">
				<div className="space-y-2">
					<div className="flex items-center justify-between">
						<h2 className="text-lg font-semibold">Recuperar senha</h2>
						<button onClick={handleForgotPasswordModal}>
							<X className="size-5 text-zinc-400 hover:text-zinc-300 transition duration-100" />
						</button>
					</div>
					<p className="text-sm text-zinc-400">
						Um e-mail será enviado para a criação de outra senha.
					</p>
				</div>

				<div className="w-full h-px bg-zinc-700" />

				<Input
					icon={<AtSign />}
					type="email"
					id="email"
					name="email"
					value={email}
					onChange={(e) => {
						setEmail(e.target.value);
					}}
					placeholder="Digite seu e-mail"
					fullSize={true}
					required
				/>
				<Button
					type="button"
					variant="secondary"
					size="full"
					onClick={sendEmail}
				>
					Enviar e-mail
				</Button>
			</div>
		</div>
	);
}
