import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import logo from '../../assets/logo.png';
import { handleLogin, storeInCache } from '../../services/auth';
import { LoginResponse } from '../../services/types';
import { ForgotPasswordModal } from './login-components/forgot-password-modal';
import { LoginForm } from './login-components/login-form';

export function LoginPage() {
	const navigate = useNavigate();
	const [isForgotPasswordModalOpen, setIsForgotPasswordModalOpen] =
		useState(false);
	const [email, setEmail] = useState('');
	const [password, setPassword] = useState('');
	const [showPassword, setShowPassword] = useState(false);

	const handleForgotPasswordModal = () => {
		setIsForgotPasswordModalOpen(!isForgotPasswordModalOpen);
	};

	const handlePasswordVisibility = (e: React.MouseEvent<HTMLButtonElement>) => {
		e.preventDefault();
		showPassword ? setShowPassword(false) : setShowPassword(true);
	};

	function UserLogIn(event: React.FormEvent<HTMLFormElement>) {
		event.preventDefault();

		handleLogin(email, password).then((data: LoginResponse) => {
			if (data) {
				storeInCache(
					data.id,
					data.accessToken,
					data.access_level,
					data.refreshToken
				);
				data.access_level === 1
					? navigate('/dashboard')
					: navigate('/professors');
			}
		});
	}

	return (
		<div className="bg-zinc-900 h-screen flex items-center justify-center">
			<div className="bg-zinc-800 w-[700px] h-auto rounded-xl pt-0 py-7 px-12 shadow-shape flex flex-col justify-center">
				<div className="w-full text-center">
					<div className="w-full">
						<img
							src={logo}
							alt="Logo Avali-e"
							className="w-52 h-auto mx-auto"
						/>
					</div>
				</div>
				<LoginForm
					UserLogIn={UserLogIn}
					email={email}
					setEmail={setEmail}
					password={password}
					setPassword={setPassword}
					showPassword={showPassword}
					handlePasswordVisibility={handlePasswordVisibility}
					handleForgotPasswordModal={handleForgotPasswordModal}
				/>
			</div>

			{isForgotPasswordModalOpen && (
				<ForgotPasswordModal
					handleForgotPasswordModal={handleForgotPasswordModal}
				/>
			)}
		</div>
	);
}
