import {
	ArrowLeftIcon,
	CircleUser,
	LayoutDashboard,
	Menu,
	Users,
	StarIcon,
} from 'lucide-react';
import { useUserAccessLevel } from '../../hooks/useUserAccessLevel';
import { MenuLink } from './menu-link';

interface MenuBarProps {
	toggleMenubar: () => void;
	isMenubarOpen: boolean;
}

export function MenuBar({ toggleMenubar, isMenubarOpen }: MenuBarProps) {
	const userAccessLevel = useUserAccessLevel();

	return (
		<div
			className={`bg-zinc-800 h-screen py-5 px-3 flex flex-col overflow-hidden shadow-shape ${
				isMenubarOpen ? 'w-64 items-center' : 'w-20 items-center'
			} transition-all duration-300`}
			style={{ position: 'fixed', left: 0, top: 0, bottom: 0 }}
		>
			<button
				onClick={toggleMenubar}
				className={`mb-2 w-full rounded-md text-zinc-300 hover:text-white hover:bg-zinc-700 p-3 flex gap-2 items-center ${
					isMenubarOpen ? 'justify-start' : 'justify-center'
				}`}
			>
				{isMenubarOpen ? <ArrowLeftIcon /> : <Menu />}
			</button>

			<nav className="w-full space-y-1">
				{userAccessLevel === 1 && (
					<MenuLink
						link="/dashboard"
						text="Dashboard"
						icon={<LayoutDashboard />}
						isMenubarOpen={isMenubarOpen}
					/>
				)}

				{userAccessLevel === 1 && (
					<MenuLink
						link="/feedback-request"
						text="Solicitar Avaliação"
						icon={<StarIcon />}
						isMenubarOpen={isMenubarOpen}
					/>
				)}

				{/* <MenuLink
					link="/report"
					text="Relatório"
					icon={<FileText />}
					isMenubarOpen={isMenubarOpen}
				/> */}

				{userAccessLevel === 2 && (
					<MenuLink
						link="/professors"
						text="Professores"
						icon={<Users />}
						isMenubarOpen={isMenubarOpen}
					/>
				)}
				<MenuLink
					link="/profile"
					text="Perfil"
					icon={<CircleUser />}
					isMenubarOpen={isMenubarOpen}
				/>
			</nav>
		</div>
	);
}
