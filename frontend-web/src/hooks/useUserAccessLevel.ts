export function useUserAccessLevel() {
	const accessLevelStr = localStorage.getItem('access_level');

	if (accessLevelStr) return parseInt(accessLevelStr);
	else return 0;
}
