import { format, subDays } from 'date-fns';
import { ptBR } from 'date-fns/locale';
import { Calendar, LogOut } from 'lucide-react';
import { useState } from 'react';
import { DateRange } from 'react-day-picker';
import { useNavigate } from 'react-router-dom';
import homeLogo from '../../assets/home-logo.png';
import { Button } from '../button';
import { DatePickerModal } from './header-components/date-picker-modal';
import { LogOutModal } from './header-components/log-out-modal';
import { useUserAccessLevel } from '../../hooks/useUserAccessLevel';
import { useUserData } from '../../hooks/useUserData';

interface HeaderProps {
	title: string;
	UserLogOut: () => void;
	hasCalendar?: boolean;
}

export function Header({
	title,
	UserLogOut,
	hasCalendar = false,
}: HeaderProps) {
	const navigate = useNavigate();
	const userData = useUserData();
	const userAccessLevel = useUserAccessLevel();
	const [isLogOutModalOpen, setIsLogOutModalOpen] = useState(false);
	const [isDatePickerOpen, setIsDatePickerOpen] = useState(false);
	const [dateRange, setDateRange] = useState<DateRange | undefined>({
		from: subDays(new Date(), 30),
		to: new Date(),
	});

	const handleDatePicker = () => {
		setIsDatePickerOpen(!isDatePickerOpen);
	};

	const handleLogOutModal = () => {
		setIsLogOutModalOpen(!isLogOutModalOpen);
	};

	const handleLogOut = () => {
		UserLogOut();
		handleLogOutModal();
	};

	const displayedDate =
		dateRange && dateRange.from && dateRange.to
			? `${format(dateRange.from, 'dd/MM/yyyy', { locale: ptBR })} até ${format(
					dateRange.to,
					'dd/MM/yyyy',
					{ locale: ptBR }
				)}`
			: null;

	function goBack() {
		const pathName = window.location.pathname;

		if (pathName === '/dashboard' || pathName === '/professors') {
			userAccessLevel === 1 ? navigate('/dashboard') : navigate('/professors');
		} else {
			navigate(-1);
		}
	}

	return (
		<div className="flex gap-3">
			<div
				className="bg-zinc-800 hover:bg-zinc-700 p-2 rounded-lg shadow-shape flex justify-between items-center cursor-pointer  transition duration-100"
				onClick={goBack}
			>
				<img
					src={homeLogo}
					alt="Logo Avali-e"
					className="w-14 h-auto mx-auto"
				/>
			</div>
			<div className="bg-zinc-800 p-4 rounded-lg shadow-shape flex justify-between items-center w-full">
				<h1 className="text-2xl font-semibold text-zinc-200">{title}</h1>
				{hasCalendar && (
					<button
						onClick={handleDatePicker}
						className="flex items-center gap-2 text-left"
					>
						<Calendar className="size-5 text-zinc-400" />
						<span className="text-lg text-zinc-400">
							{displayedDate || 'Selecione o período'}
						</span>
					</button>
				)}
				{userData &&
					(window.location.pathname === '/dashboard' ||
						window.location.pathname === '/professors') && (
						<div className="text-zinc-500 flex gap-2 items-baseline ml-[-150px]">
							Olá, {userAccessLevel === 1 ? 'Professor' : 'Diretor'}
							<span className="text-zinc-300 font-bold text-xl">
								{userData.name}
							</span>
						</div>
					)}
				<Button type="button" variant="secondary" onClick={handleLogOutModal}>
					<LogOut />
					Sair
				</Button>
				{isLogOutModalOpen && (
					<LogOutModal
						handleLogOutModal={handleLogOutModal}
						handleLogOut={handleLogOut}
					/>
				)}
				{isDatePickerOpen && (
					<DatePickerModal
						handleDatePicker={handleDatePicker}
						dateRange={dateRange}
						setDateRange={setDateRange}
					/>
				)}
			</div>
		</div>
	);
}
