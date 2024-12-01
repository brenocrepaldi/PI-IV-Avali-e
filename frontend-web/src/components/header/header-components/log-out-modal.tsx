import { Button } from '../../button';

interface LogOutModalProps {
	handleLogOutModal: () => void;
	handleLogOut: () => void;
}

export function LogOutModal({
	handleLogOutModal,
	handleLogOut,
}: LogOutModalProps) {
	return (
		<div className="fixed inset-0 bg-black/60 flex items-center justify-center z-50">
			<div className="rounded-xl py-7 px-8 shadow-shape bg-zinc-900 space-y-5">
				<h2 className="text-xl font-semibold text-zinc-200">
					Tem certeza que deseja sair?
				</h2>
				<p className="text-zinc-400">
					Isso irá desconectar você e você precisará entrar novamente para
					acessar a conta.
				</p>
				<div className="flex justify-end gap-2">
					<Button variant="secondary" onClick={handleLogOutModal}>
						Cancelar
					</Button>
					<Button variant="tertiary" onClick={handleLogOut}>
						Sair
					</Button>
				</div>
			</div>
		</div>
	);
}
