
// ------------------User controller--------------------------

export const getUsers = () => fetch(`${import.meta.env.VITE_APP_API_URI}/users`);

export const getUserById = (_id) => fetch(`${import.meta.env.VITE_APP_API_URI}/users/${_id}`);

export const createUser = (user) => fetch(`${import.meta.env.VITE_APP_API_URI}/users`, {
    headers: {
        'Content-Type': 'application/json'
    },
    method: 'POST',
    body: JSON.stringify(user)
});

export const updateUserById = (_id, updatedUser) => fetch(`${import.meta.env.VITE_APP_API_URI}/users/${_id}`, {
    headers: {
        'Content-Type': 'application/json'
    },
    method: 'PUT',
    body: JSON.stringify(updatedUser)
});

export const deleteUserById = (_id) => fetch(`${import.meta.env.VITE_APP_API_URI}/users/${_id}`, {
    method: 'DELETE'
});

// ------------------User controller--------------------------

// ------------------Manager controller-----------------------

export const getManagers = () => fetch(`${import.meta.env.VITE_APP_API_URI}/managers`);

export const getManagerById = (_id) => fetch(`${import.meta.env.VITE_APP_API_URI}/managers/${_id}`);

export const createManager = (manager) => fetch(`${import.meta.env.VITE_APP_API_URI}/managers`, {
    headers: {
        'Content-Type': 'application/json'
    },
    method: 'POST',
    body: JSON.stringify(manager)
});

export const updateManagerById = (_id, updatedManager) => fetch(`${import.meta.env.VITE_APP_API_URI}/managers/${_id}`, {
    headers: {
        'Content-Type': 'application/json'
    },
    method: 'PUT',
    body: JSON.stringify(updatedManager)
});

export const deleteManagerById = (_id) => fetch(`${import.meta.env.VITE_APP_API_URI}/managers/${_id}`, {
    method: 'DELETE'
});

// ------------------Manager controller-----------------------

// ------------------Candidate controller---------------------

export const getCandidates = () => fetch(`${import.meta.env.VITE_APP_API_URI}/candidates`);

export const getCandidateById = (_id) => fetch(`${import.meta.env.VITE_APP_API_URI}/candidates/${_id}`);

export const createCandidate = (candidate) => fetch(`${import.meta.env.VITE_APP_API_URI}/candidates`, {
    headers: {
        'Content-Type': 'application/json'
    },
    method: 'POST',
    body: JSON.stringify(candidate)
});

export const updateCandidateById = (_id, updatedCandidate) => fetch(`${import.meta.env.VITE_APP_API_URI}/candidate/${_id}`, {
    headers: {
        'Content-Type': 'application/json'
    },
    method: 'PUT',
    body: JSON.stringify(updatedCandidate)
});

export const deleteCandidateById = (_id) => fetch(`${import.meta.env.VITE_APP_API_URI}/candidates/${_id}`, {
    method: 'DELETE'
});

// ------------------Candidate controller---------------------

// ------------------Job controller---------------------------

export const getJobs = () => fetch(`${import.meta.env.VITE_APP_API_URI}/jobs`);

export const getJobById = (_id) => fetch(`${import.meta.env.VITE_APP_API_URI}/jobs/${_id}`);

export const createJob = (job) => fetch(`${import.meta.env.VITE_APP_API_URI}/jobs`, {
    headers: {
        'Content-Type': 'application/json'
    },
    method: 'POST',
    body: JSON.stringify(job)
});

export const updateJobById = (_id, updatedJob) => fetch(`${import.meta.env.VITE_APP_API_URI}/jobs/${_id}`, {
    headers: {
        'Content-Type': 'application/json'
    },
    method: 'PUT',
    body: JSON.stringify(updatedJob)
});

export const deleteJobById = (_id) => fetch(`${import.meta.env.VITE_APP_API_URI}/jobs/${_id}`, {
    method: 'DELETE'
});

export const getJobsByManagerId = (managerId) => fetch(`${import.meta.env.VITE_APP_API_URI}/jobs/manager/${managerId}`)

// ------------------Job controller---------------------------

// ------------------Application controller-------------------

export const getApplications = () => fetch(`${import.meta.env.VITE_APP_API_URI}/applications`);

export const getApplicationById = (_id) => fetch(`${import.meta.env.VITE_APP_API_URI}/applications/${_id}`);

export const createApplication = (application) => fetch(`${import.meta.env.VITE_APP_API_URI}/applicaions`, {
    headers: {
        'Content-Type': 'application/json'
    },
    method: 'POST',
    body: JSON.stringify(application)
});

export const updateApplicationById = (_id, updatedApplication) => fetch(`${import.meta.env.VITE_APP_API_URI}/applications/${_id}`, {
    headers: {
        'Content-Type': 'application/json'
    },
    method: 'PUT',
    body: JSON.stringify(updatedApplication)
});

export const deleteApplicationById = (_id) => fetch(`${import.meta.env.VITE_APP_API_URI}/applications/${_id}`, {
    method: 'DELETE'
});

export const getApplicationsByJobId = (jobId) => fetch(`${import.meta.env.VITE_APP_API_URI}/applications/job/${jobId}`)

export const getApplicationsByCandidateId = (candidateId) => fetch(`${import.meta.env.VITE_APP_API_URI}/applications/candidate/${candidateId}`)

// ------------------Application controller-------------------
