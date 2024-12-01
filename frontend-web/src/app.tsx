import {
	Navigate,
	Route,
	BrowserRouter as Router,
	Routes,
} from 'react-router-dom';
import { AddProfessorPage } from './pages/add-professor';
import { DashboardPage } from './pages/dashboard/index';
import { LoginPage } from './pages/login/index';
import { NotFoundPage } from './pages/not-found';
import { ProfilePage } from './pages/profile';
import { FeedbacksPage } from './pages/feedbacks';
import { ProfessorsPage } from './pages/professors';
import { PrivateRoute } from './routes/private-route';
import { RequestFeedbackPage } from './pages/request-feedback';

export function App() {
	return (
		<Router>
			<Routes>
				<Route path="/" element={<Navigate to="/login" />} />
				<Route path="/login" element={<LoginPage />} />

				<Route
					path="/dashboard"
					element={
						<PrivateRoute requiredLevel={1}>
							<DashboardPage />
						</PrivateRoute>
					}
				/>
				<Route
					path="/feedback-request"
					element={
						<PrivateRoute requiredLevel={1}>
							<RequestFeedbackPage />
						</PrivateRoute>
					}
				/>

				<Route
					path="/feedbacks"
					element={
						<PrivateRoute>
							<FeedbacksPage />
						</PrivateRoute>
					}
				/>

				<Route
					path="/professors"
					element={
						<PrivateRoute requiredLevel={2}>
							<ProfessorsPage />
						</PrivateRoute>
					}
				/>
				<Route
					path="/professors/add-professor"
					element={
						<PrivateRoute requiredLevel={2}>
							<AddProfessorPage />
						</PrivateRoute>
					}
				/>

				<Route
					path="/profile"
					element={
						<PrivateRoute>
							<ProfilePage />
						</PrivateRoute>
					}
				/>

				<Route path="/not-found" element={<NotFoundPage />} />
			</Routes>
		</Router>
	);
}
