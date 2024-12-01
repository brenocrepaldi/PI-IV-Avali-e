import { PageLayout } from '../../components/page-layout';

export function ReportPage() {
	const feedbackData = [
		{ id: 1, tipo: 'Positivas', quantidade: 28, cor: '#3b82f6' },
		{ id: 2, tipo: 'Neutras', quantidade: 14, cor: '#71717a' },
		{ id: 3, tipo: 'Negativas', quantidade: 5, cor: '#ef4444' },
	];

	const gradesData = [
		{ turma: 'Turma 1', positivas: 30, neutras: 12, negativas: 7 },
		{ turma: 'Turma 2', positivas: 34, neutras: 13, negativas: 9 },
		{ turma: 'Turma 3', positivas: 43, neutras: 10, negativas: 4 },
	];

	return (
		<PageLayout title="Relatório">
			<div className="bg-zinc-800 p-6 pb-8 rounded-lg shadow-shape text-zinc-300 flex-grow">
				<div className="bg-zinc-700 py-6 px-3 rounded-md flex flex-col flex-grow">
					<h2 className="text-lg font-semibold mb-4">
						Estatísticas de Avaliação
					</h2>

					{/* Tabela de Feedback */}
					<h3 className="text-md font-semibold mb-2">Feedback dos Alunos</h3>
					<table className="min-w-full bg-zinc-600 rounded-lg shadow-md border-separate border-spacing-2">
						<thead>
							<tr className="text-left">
								<th className="py-3 px-4">Tipo</th>
								<th className="py-3 px-4">Quantidade</th>
							</tr>
						</thead>
						<tbody>
							{feedbackData.map((item) => (
								<tr
									key={item.id}
									className="hover:bg-zinc-500 transition duration-100"
								>
									<td className="py-3 px-4 rounded-lg hover:bg-zinc-700 transition duration-100">
										{item.tipo}
									</td>
									<td
										className="py-3 px-4 rounded-lg hover:bg-zinc-700 transition duration-100"
										style={{ color: item.cor }}
									>
										{item.quantidade}
									</td>
								</tr>
							))}
						</tbody>
					</table>

					{/* Tabela de Notas */}
					<h3 className="text-md font-semibold mt-6 mb-2">Notas por Turma</h3>
					<table className="min-w-full bg-zinc-600 rounded-lg shadow-md border-separate border-spacing-2">
						<thead>
							<tr className="text-left">
								<th className="py-3 px-4">Turma</th>
								<th className="py-3 px-4">Positivas</th>
								<th className="py-3 px-4">Neutras</th>
								<th className="py-3 px-4">Negativas</th>
							</tr>
						</thead>
						<tbody>
							{gradesData.map((item, index) => (
								<tr
									key={index}
									className="hover:bg-zinc-500 transition duration-100"
								>
									<td className="py-3 px-4 rounded-lg hover:bg-zinc-700 transition duration-100">
										{item.turma}
									</td>
									<td className="py-3 px-4 rounded-lg hover:bg-zinc-700 transition duration-100">
										{item.positivas}
									</td>
									<td className="py-3 px-4 rounded-lg hover:bg-zinc-700 transition duration-100">
										{item.neutras}
									</td>
									<td className="py-3 px-4 rounded-lg hover:bg-zinc-700 transition duration-100">
										{item.negativas}
									</td>
								</tr>
							))}
						</tbody>
					</table>
				</div>
			</div>
		</PageLayout>
	);
}
