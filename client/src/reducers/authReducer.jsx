const initialState = {
    isAuthenticated: false,
    user: null, // Kullanıcı bilgisi için eklenen alan
  };
  
  const authReducer = (state = initialState, action) => {
    switch (action.type) {
      case "LOGIN":
        return { ...state, isAuthenticated: true, user: action.payload };
      case "LOGOUT":
        return { ...state, isAuthenticated: false, user: null };
      default:
        return state;
    }
  };
  
  export default authReducer;
  