import { ReactNode, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { handleLogout } from '../services/auth';
import { Header } from './header';
import { MenuBar } from './menu-bar/index';

interface PageLayoutProps {
	title: string;
	hasCalendar?: boolean;
	children: ReactNode;
}

export function PageLayout({ title, hasCalendar, children }: PageLayoutProps) {
	const navigate = useNavigate();
	const [isMenubarOpen, setIsMenubarOpen] = useState(false);
	const [contentVisible, setContentVisible] = useState(false);

	const toggleMenubar = () => {
		setIsMenubarOpen(!isMenubarOpen);
	};

	useEffect(() => {
		if (isMenubarOpen) {
			setContentVisible(false);
			setTimeout(() => setContentVisible(true), 400);
		} else {
			setContentVisible(false);
			setTimeout(() => setContentVisible(true), 400);
		}
	}, [isMenubarOpen]);

	function UserLogOut() {
		handleLogout();
		navigate('/login');
	}

	return (
		<div className="bg-zinc-900 h-screen flex">
			<MenuBar toggleMenubar={toggleMenubar} isMenubarOpen={isMenubarOpen} />
			<div
				className={`flex-1 p-10 transition-all duration-300 overflow-y-scroll overflow-x-hidden flex flex-col gap-6 ${
					isMenubarOpen ? 'ml-64' : 'ml-20'
				}`}
			>
				<Header
					title={title}
					UserLogOut={UserLogOut}
					hasCalendar={hasCalendar}
				/>
				<div
					className={`flex flex-col gap-6 transition-all duration-300 ${
						contentVisible ? 'opacity-100' : 'opacity-0'
					}`}
				>
					{children}
				</div>
			</div>
		</div>
	);
}
