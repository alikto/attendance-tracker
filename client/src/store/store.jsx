import rootReducer from "../reducers/rootReducer";
import { thunk } from "redux-thunk";
import { createStore, applyMiddleware } from "redux";
import { persistStore, persistReducer } from "redux-persist";
import storage from "redux-persist/lib/storage";

const persistConfig = {
  key: "root", // key for the localStorage object where your state will be saved
  storage, // define which storage to use
  blacklist: [], // array of reducer keys to ignore for persistence
  whitelist: [], // only persist specified reducers, uncomment to use
};

const persistedReducer = persistReducer(persistConfig, rootReducer);

export const store = createStore(persistedReducer, applyMiddleware(thunk));

// Enable persistence
export let persistor = persistStore(store);