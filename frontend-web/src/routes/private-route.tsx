import React from 'react';
import { Navigate } from 'react-router-dom';
import { useUserAccessLevel } from '../hooks/useUserAccessLevel';

interface PrivateRouteProps {
	children: React.ReactNode;
	requiredLevel?: number;
}

export function PrivateRoute({ children, requiredLevel }: PrivateRouteProps) {
	const userAccessLevel = useUserAccessLevel();

	if (
		!userAccessLevel ||
		(requiredLevel && userAccessLevel !== requiredLevel)
	) {
		return <Navigate to="/not-found" />;
	}

	return <>{children}</>;
}
