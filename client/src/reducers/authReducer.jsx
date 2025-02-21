import { LOGIN_SUCCESS, LOGIN_FAIL, LOGOUT } from "../actions/authActions";

const initialState = {
    token: localStorage.getItem("token") || null,
    user: JSON.parse(localStorage.getItem("user")) || null,
    isAuthenticated: !!localStorage.getItem("token"),
    error: null
};

const authReducer = (state = initialState, action) => {
    switch (action.type) {
        case LOGIN_SUCCESS:
            return {
                ...state,
                token: action.payload.token,
                user: action.payload.user,
                isAuthenticated: true,
                error: null
            };
        case LOGIN_FAIL:
            return { ...state, error: action.payload };
        case LOGOUT:
            return { ...state, token: null, user: null, isAuthenticated: false };
        default:
            return state;
    }
};

export default authReducer;
