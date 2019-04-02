import { GraphqlClient, GraphqlStore } from 'oo-graphql-mobx'
import config from '../utils/config'

const defaultVariables = { token: 'gorm-dev-token' }

export const graphqlClient = new GraphqlClient({
  uri: config.graphqlUri, defaultVariables
})


//const messageListener = message => taroAtMessage({ ...message, message: message.text })
const storeParam = { graphqlClient } //, messageListeners: [messageListener] }

const paramStore = new GraphqlStore({ domain: 'param', ...storeParam })
const menuStore = new GraphqlStore({ domain: 'menu', ...storeParam })

export default {
  paramStore,
  menuStore
}

