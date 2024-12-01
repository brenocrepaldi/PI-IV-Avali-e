export function useUserId() {
	const accessLevel = localStorage.getItem('id');

	if (accessLevel) return accessLevel;
	else return 0;
}
