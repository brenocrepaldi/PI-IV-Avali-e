import { PageLayout } from '../../components/page-layout';
import { Statistics } from './dashboard-components/statistics';
import { LastFeedbacks } from './dashboard-components/last-feedbacks';

export function DashboardPage() {
	return (
		<PageLayout title="Dashboard">
			<Statistics />
			<LastFeedbacks />
		</PageLayout>
	);
}
