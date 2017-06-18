import { User } from '../../shared';
export class Projeto {
    constructor(
        public id?: number,
        public nome?: string,
        public caminho?: string,
        public arquivos?: string,
        public user?: User,
    ) {
    }
}
