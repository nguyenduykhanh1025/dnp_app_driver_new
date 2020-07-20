import { createStore, applyMiddleware, compose } from 'redux';
import createSagaMiddleware from 'redux-saga';
import { persistStore, persistReducer } from 'redux-persist';
import AsyncStorage from '@react-native-community/async-storage';
// import immutableTransform from 'redux-persist-transform-immutable';
import rootReducer from './reducers';
import rootSaga from './sagas';

const persistConfig = {
    key: 'root',
    // transforms: [immutableTransform()],
    storage: AsyncStorage,
    whitelist: ['auth'],
};

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

const sagaMiddleware = createSagaMiddleware();

const persistedReducer = persistReducer(persistConfig, rootReducer);

export default () => {
    const enhancer = composeEnhancers(applyMiddleware(sagaMiddleware));

    const store = createStore(persistedReducer, enhancer);
    let persistor = persistStore(store);

    // then run the saga
    sagaMiddleware.run(rootSaga);

    return { store, persistor };
};
