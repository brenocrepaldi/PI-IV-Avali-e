interface MenuLinkProps {
	link: string;
	text: string;
	icon: React.ReactNode;
	isMenubarOpen: boolean;
}

export function MenuLink({ link, text, icon, isMenubarOpen }: MenuLinkProps) {
	return (
		<a
			href={link}
			className={`text-zinc-300 hover:bg-zinc-700 p-3 w-full rounded-md flex gap-2 items-center ${
				isMenubarOpen ? 'justify-start' : 'justify-center'
			}`}
		>
			{icon}
			{isMenubarOpen && text}
		</a>
	);
}
