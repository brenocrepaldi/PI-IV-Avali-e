export function NotFoundPage() {
	console.log(localStorage.getItem('access_level'));
	return (
		<div className="flex items-center justify-center h-screen bg-zinc-900 text-white">
			<div className="text-center">
				<h1 className="text-6xl font-bold text-red-500">404</h1>
				<p className="mt-4 text-xl text-gray-300">Página não encontrada</p>
				<a href="/" className="mt-6 text-sky-400 hover:underline">
					Voltar para a página inicial
				</a>
			</div>
		</div>
	);
}
