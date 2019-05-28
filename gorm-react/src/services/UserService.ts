import DomainService from 'oo-graphql-service/lib/DomainService';
import { Entity, MobxDomainStore } from 'oo-graphql-service';
import gql from 'graphql-tag';
import { sha256 } from 'js-sha256'
import { message } from 'antd';
import DomainGraphql from 'oo-graphql-service/lib/DomainGraphql';

export interface LoginInfo {
  success: boolean
  token: string
  user: Entity
  error: string
}

const USERNAME_KEY = 'loginUsername'
const PASSWORD_KEY = 'loginPassword'

export default class UserService extends DomainService<MobxDomainStore> {

  constructor(private afterLogin: (info: LoginInfo) => void, domainGraphql: DomainGraphql) {
    super('user', MobxDomainStore, domainGraphql);
    this.changeCurrentItem({})
  }

  login(username: string, password: string, remember: boolean = false): Promise<any> {
    return this.loginHash(username, sha256(password), remember)
  }

  loginHash(username: string, passwordHash: string, remember: boolean = false): Promise<any> {
    return this.domainGraphql.apolloClient.mutate<{ login: LoginInfo }>({
      mutation: gql`mutation loginAction {
                      login(username: "${username}", password: "${passwordHash}") {
                        success
                        error
                        token
                        user{
                          id, account, name      
                        }
                      }
                    }`,
      fetchPolicy: 'no-cache',
      variables: {
        ...this.domainGraphql.defaultVariables
      }
    })
      .then(data => {
        const login = data.data!.login
        if (login.success) {
          this.afterLogin(login)
          this.changeCurrentItem(login.user)
          if (remember)
            this.saveLoginInfoLocal(username, passwordHash)
        } else {
          message.info(login.error);
          this.clearLoginInfoLocal();
        }
        return login
      })
  }

  tryLocalLogin() {
    const info = this.getLoginInfoLocal()
    if (info.username && info.password)
      this.loginHash(info.username, info.password)
  }

  saveLoginInfoLocal(username: string, password: string) {
    localStorage.setItem(USERNAME_KEY, username)
    localStorage.setItem(PASSWORD_KEY, password)
  }

  clearLoginInfoLocal() {
    localStorage.removeItem(USERNAME_KEY)
    localStorage.removeItem(PASSWORD_KEY)
  }

  getLoginInfoLocal() {
    return {
      username: localStorage.getItem(USERNAME_KEY),
      password: localStorage.getItem(PASSWORD_KEY)
    }
  }
}
