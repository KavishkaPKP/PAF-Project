import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Formik, Form } from 'formik';
import * as Yup from 'yup';
import { Button, Typography, Box, Alert, Chip, Stack, TextField } from '@mui/material';
import authService from '../api/auth';

const RegisterSchema = Yup.object().shape({
  username: Yup.string().required('Username is required'),
  email: Yup.string().email('Invalid email').required('Email is required'),
  password: Yup.string()
    .required('Password is required')
    .min(8, 'Password must be at least 8 characters')
    .matches(
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])(?=.{8,})/,
      'Must contain at least one uppercase, one lowercase, one number and one special case character'
    ),
  bio: Yup.string(),
  profilePicture: Yup.string().url('Must be a valid URL'),
});

const Register = () => {
  const [error, setError] = useState('');
  const [skills, setSkills] = useState([]);
  const [currentSkill, setCurrentSkill] = useState('');
  const navigate = useNavigate();

  const handleAddSkill = () => {
    if (currentSkill && !skills.includes(currentSkill)) {
      setSkills([...skills, currentSkill]);
      setCurrentSkill('');
    }
  };

  const handleRemoveSkill = (skillToRemove) => {
    setSkills(skills.filter((skill) => skill !== skillToRemove));
  };

  return (
    <Box sx={{ maxWidth: 600, mx: 'auto', mt: 4 }}>
      <Typography variant="h4" component="h1" gutterBottom>
        Register
      </Typography>
      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
      <Formik
        initialValues={{
          username: '',
          email: '',
          password: '',
          bio: '',
          profilePicture: '',
        }}
        validationSchema={RegisterSchema}
        onSubmit={async (values, { setSubmitting }) => {
          try {
            await authService.register(
              values.username,
              values.email,
              values.password,
              values.bio,
              values.profilePicture,
              skills
            );
            navigate('/login');
          } catch (err) {
            setError(err.response?.data?.message || 'Registration failed');
          } finally {
            setSubmitting(false);
          }
        }}
      >
        {({ isSubmitting, errors, touched, handleChange, handleBlur, values }) => (
          <Form>
            <TextField
              name="username"
              label="Username"
              fullWidth
              margin="normal"
              value={values.username}
              onChange={handleChange}
              onBlur={handleBlur}
              error={touched.username && Boolean(errors.username)}
              helperText={touched.username && errors.username}
            />
            <TextField
              name="email"
              label="Email"
              fullWidth
              margin="normal"
              value={values.email}
              onChange={handleChange}
              onBlur={handleBlur}
              error={touched.email && Boolean(errors.email)}
              helperText={touched.email && errors.email}
            />
            <TextField
              name="password"
              label="Password"
              type="password"
              fullWidth
              margin="normal"
              value={values.password}
              onChange={handleChange}
              onBlur={handleBlur}
              error={touched.password && Boolean(errors.password)}
              helperText={touched.password && errors.password}
            />
            <TextField
              name="bio"
              label="Bio"
              multiline
              rows={3}
              fullWidth
              margin="normal"
              value={values.bio}
              onChange={handleChange}
              onBlur={handleBlur}
              error={touched.bio && Boolean(errors.bio)}
              helperText={touched.bio && errors.bio}
            />
            <TextField
              name="profilePicture"
              label="Profile Picture URL"
              fullWidth
              margin="normal"
              value={values.profilePicture}
              onChange={handleChange}
              onBlur={handleBlur}
              error={touched.profilePicture && Boolean(errors.profilePicture)}
              helperText={touched.profilePicture && errors.profilePicture}
            />
            
            <Box sx={{ mt: 2 }}>
              <Typography variant="subtitle1" gutterBottom>
                Skills
              </Typography>
              <Stack direction="row" spacing={1} sx={{ mb: 2 }}>
                {skills.map((skill) => (
                  <Chip
                    key={skill}
                    label={skill}
                    onDelete={() => handleRemoveSkill(skill)}
                  />
                ))}
              </Stack>
              <Box sx={{ display: 'flex', gap: 1 }}>
                <TextField
                  value={currentSkill}
                  onChange={(e) => setCurrentSkill(e.target.value)}
                  label="Add Skill"
                  size="small"
                />
                <Button
                  variant="outlined"
                  onClick={handleAddSkill}
                  disabled={!currentSkill}
                >
                  Add
                </Button>
              </Box>
            </Box>
            
            <Button
              type="submit"
              variant="contained"
              color="primary"
              disabled={isSubmitting}
              fullWidth
              sx={{ mt: 3 }}
            >
              Register
            </Button>
          </Form>
        )}
      </Formik>
    </Box>
  );
};

export default Register;