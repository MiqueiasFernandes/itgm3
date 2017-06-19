import { Projeto } from '../projeto';
export class Cenario {
    constructor(
        public id?: number,
        public nome?: string,
        public caminho?: string,
        public projeto?: Projeto,
    ) {
    }
}
