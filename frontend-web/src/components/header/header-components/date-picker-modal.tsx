import { X } from 'lucide-react';
import {
	DateRange,
	DayPicker,
	SelectRangeEventHandler,
} from 'react-day-picker';

interface DatePickerModalProps {
	handleDatePicker: () => void;
	dateRange: DateRange | undefined;
	setDateRange: SelectRangeEventHandler;
}

export function DatePickerModal({
	handleDatePicker,
	dateRange,
	setDateRange,
}: DatePickerModalProps) {
	return (
		<div className="fixed inset-0 bg-black/60 flex items-center justify-center z-50">
			<div className="rounded-xl py-7 px-8 shadow-shape bg-zinc-900 space-y-5">
				<div className="space-y-2">
					<div className="flex items-center justify-between">
						<h2 className="text-xl font-semibold text-zinc-200">
							Selecione o período
						</h2>
						<button onClick={handleDatePicker}>
							<X className="size-5 text-zinc-400" />
						</button>
					</div>
				</div>
				<DayPicker
					className="custom-day-picker"
					mode="range"
					selected={dateRange}
					onSelect={setDateRange}
				/>
			</div>
		</div>
	);
}
