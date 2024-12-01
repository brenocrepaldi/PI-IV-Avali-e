import { DisciplineNote } from '../hooks/useFeedbacks';

export const getFeedbackOption = (
	positives: number,
	neutral: number,
	negatives: number
) => {
	return {
		tooltip: {
			trigger: 'item',
		},
		series: [
			{
				name: 'Feedback',
				type: 'pie',
				radius: ['40%', '70%'],
				avoidLabelOverlap: false,
				padAngle: 5,
				itemStyle: {
					borderRadius: 10,
				},
				data: [
					{
						value: positives,
						name: 'Positivas',
						itemStyle: { color: '#3b82f6' },
					},
					{
						value: neutral,
						name: 'Neutras',
						itemStyle: { color: '#71717a' },
					},
					{
						value: negatives,
						name: 'Negativas',
						itemStyle: { color: '#ef4444' },
					},
				],
				emphasis: {
					itemStyle: {
						shadowBlur: 10,
						shadowOffsetX: 0,
						shadowColor: 'rgba(0, 0, 0, 0.5)',
					},
				},
				label: {
					color: '#ffffff',
					fontFamily: 'Inter, sans-serif',
				},
			},
		],
	};
};

export const getGradesOption = (
	disciplineNotesInfo: DisciplineNote[] | undefined
) => {
	return {
		tooltip: {
			trigger: 'axis',
			axisPointer: {
				type: 'line',
			},
		},
		grid: {
			left: '5%',
			right: '5%',
			bottom: '5%',
			containLabel: true,
		},
		xAxis: {
			type: 'value',
		},
		yAxis: {
			type: 'category',
			data: disciplineNotesInfo?.map((item) => item.disciplineName),
		},
		series: [
			{
				name: 'Positivas',
				type: 'bar',
				stack: 'total',
				label: {
					show: true,
					color: '#ffffff',
					fontFamily: 'Inter, sans-serif',
					formatter: (params: { value: number }) =>
						params.value > 0 ? `${params.value}` : '',
				},
				emphasis: {
					focus: 'series',
				},
				data: disciplineNotesInfo?.map((item) => item.positiveNotes || 0), // Valor mínimo para exibir
				itemStyle: {
					color: '#3b82f6',
					borderRadius: [0, 7, 7, 0],
				},
			},
			{
				name: 'Neutras',
				type: 'bar',
				stack: 'total',
				label: {
					show: true,
					color: '#ffffff',
					fontFamily: 'Inter, sans-serif',
					formatter: (params: { value: number }) =>
						params.value > 0 ? `${params.value}` : '',
				},
				emphasis: {
					focus: 'series',
				},
				data: disciplineNotesInfo?.map((item) => item.neutralNotes || 0),
				itemStyle: {
					color: '#71717a',
					borderRadius: [7, 7, 7, 7],
				},
			},
			{
				name: 'Negativas',
				type: 'bar',
				stack: 'total',
				label: {
					show: true,
					color: '#ffffff',
					fontFamily: 'Inter, sans-serif',
					formatter: (params: { value: number }) =>
						params.value > 0 ? `${params.value}` : '',
				},
				emphasis: {
					focus: 'series',
				},
				data: disciplineNotesInfo?.map((item) => item.negativeNotes || 0),
				itemStyle: {
					color: '#ef4444',
					borderRadius: [7, 7, 7, 7],
				},
			},
		],
	};
};
