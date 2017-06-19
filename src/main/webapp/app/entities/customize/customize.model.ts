import { User } from '../../shared';
import { Projeto } from '../projeto';
import { Cenario } from '../cenario';
export class Customize {
    constructor(
        public id?: number,
        public sidebar?: boolean,
        public color?: string,
        public avatar?: any,
        public desktop?: string,
        public user?: User,
        public projeto?: Projeto,
        public cenario?: Cenario,
    ) {
        this.sidebar = false;
    }
}
