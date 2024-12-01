import { Button } from '../../../components/button';
import { Input } from '../../../components/input';

interface LoginFormProps {
	UserLogIn: (event: React.FormEvent<HTMLFormElement>) => void;
	email: string;
	setEmail: React.Dispatch<React.SetStateAction<string>>;
	password: string;
	setPassword: React.Dispatch<React.SetStateAction<string>>;
	showPassword: boolean;
	handlePasswordVisibility: (e: React.MouseEvent<HTMLButtonElement>) => void;
	handleForgotPasswordModal: () => void;
}

export function LoginForm({
	UserLogIn,
	email,
	setEmail,
	password,
	setPassword,
	showPassword,
	handlePasswordVisibility,
	handleForgotPasswordModal,
}: LoginFormProps) {
	return (
		<form className="space-y-3" onSubmit={UserLogIn}>
			<p className="text-sm text-zinc-400">
				Preencha seus dados para realizar o login.
			</p>
			<div className="space-y-2">
				<Input
					type="email"
					name="email"
					placeholder="Digite seu e-mail"
					value={email}
					onChange={(e) => setEmail(e.target.value)}
					className="bg-transparent text-base placeholder-zinc-400 outline-none flex-1"
					required
				/>
				<Input
					isPassword={true}
					handlePasswordVisibility={handlePasswordVisibility}
					showPassword={showPassword}
					type={showPassword ? 'text' : 'password'}
					name="password"
					placeholder="Digite sua senha"
					value={password}
					onChange={(e) => setPassword(e.target.value)}
					className="bg-transparent text-base placeholder-zinc-400 outline-none flex-1"
					required
				/>
			</div>
			<div className="space-y-2">
				<Button type="submit" variant="primary" size="full">
					Entrar
				</Button>
				<div className="w-full flex justify-center">
					<button
						type="button"
						className="text-sm text-zinc-300 underline hover:text-zinc-200"
						onClick={handleForgotPasswordModal}
					>
						Esqueci minha senha
					</button>
				</div>
			</div>
		</form>
	);
}
