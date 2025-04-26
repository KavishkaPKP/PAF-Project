import { useEffect, useState } from 'react';
import { Typography, Box, Avatar, Chip, CircularProgress, Alert } from '@mui/material';
import userService from '../api/users';

const Profile = () => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await userService.getCurrentUser();
        setUser(response.data);
      } catch (err) {
        setError(err.response?.data?.message || 'Failed to fetch profile');
      } finally {
        setLoading(false);
      }
    };

    fetchProfile();
  }, []);

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Box sx={{ mt: 4 }}>
        <Alert severity="error">{error}</Alert>
      </Box>
    );
  }

  return (
    <Box sx={{ maxWidth: 600, mx: 'auto', mt: 4 }}>
      <Box sx={{ display: 'flex', alignItems: 'center', mb: 4 }}>
        <Avatar
          src={user?.profilePicture}
          alt={user?.username}
          sx={{ width: 100, height: 100, mr: 3 }}
        />
        <Box>
          <Typography variant="h4" component="h1">
            {user?.username}
          </Typography>
          <Typography variant="subtitle1" color="text.secondary">
            {user?.email}
          </Typography>
        </Box>
      </Box>
      
      <Typography variant="h6" gutterBottom>
        About
      </Typography>
      <Typography paragraph sx={{ mb: 3 }}>
        {user?.bio || 'No bio provided'}
      </Typography>
      
      <Typography variant="h6" gutterBottom>
        Skills
      </Typography>
      <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1, mb: 3 }}>
        {user?.skills?.length > 0 ? (
          user.skills.map((skill) => (
            <Chip key={skill} label={skill} />
          ))
        ) : (
          <Typography color="text.secondary">No skills added</Typography>
        )}
      </Box>
    </Box>
  );
};

export default Profile;