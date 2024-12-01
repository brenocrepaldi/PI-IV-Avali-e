import { ReactNode, ComponentProps } from 'react';
import { tv, VariantProps } from 'tailwind-variants';

const buttonVariants = tv({
	base: 'rounded-lg px-5 font-medium flex items-center justify-center gap-2 transition duration-100',

	variants: {
		variant: {
			primary: 'bg-sky-400 text-sky-950 hover:bg-sky-500',
			secondary: 'bg-zinc-600 text-zinc-200 hover:bg-zinc-700',
			tertiary: 'bg-red-600 text-zinc-200 hover:bg-red-700',
		},

		size: {
			default: 'py-2',
			full: 'w-full h-11',
		},
	},

	defaultVariants: {
		variant: 'primary',
		size: 'default',
	},
});

interface ButtonProps
	extends ComponentProps<'button'>,
		VariantProps<typeof buttonVariants> {
	children: ReactNode;
}

export function Button({ children, variant, size, ...props }: ButtonProps) {
	return (
		<button {...props} className={buttonVariants({ variant, size })}>
			{children}
		</button>
	);
}
