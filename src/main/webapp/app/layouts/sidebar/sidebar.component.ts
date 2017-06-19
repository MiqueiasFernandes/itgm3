import {Component, Input, OnInit} from '@angular/core';
import { Router } from '@angular/router';

import {EventManager} from 'ng-jhipster';
import {NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {Account, AccountService, Principal} from '../../shared';
import {SidebarService} from './sidebar.service';

import {
    Projeto,
    Cenario,
    CenarioService,
    Base,
    BaseService,
    Modelo,
    ModeloService,
    SelecionarProjetoComponent,
    SelecionarCenarioComponent,
    ModeloExclusivo,
    ModeloExclusivoService,
    MapearModeloComponent,
    FabAddBaseComponent,
    FabAddModeloComponent,
    Customize,
    CustomizeService,
} from '../../entities/';

import { ShareService } from '../share/share.service';

import { FolderComponent } from './folder/folder.component';

@Component({
    selector: 'jhi-sidebar',
    templateUrl: './sidebar.component.html',
    styleUrls: ['./sidebar.scss'],
    entryComponents: [
        FolderComponent,
    ]
})
export class SidebarComponent implements OnInit {

    private isViewInitialized:boolean = false;

    isSidebarFixed = false;
    nome = '';
    email = '';
    image = null;
    projeto: Projeto = undefined;
    cenario: Cenario = undefined;
    isbasesOpen = false;
    isModelosOpen = false;
    isArquivosOpen = false;
    bases: Base[] = null;
    modelos: Modelo[] = null;
    modelosMapeados: number[] = [];
    customize: Customize = null;
    arquivos = [];
    loadArquivos = false;
    loadBases = false;
    loadModelos = false;

    constructor(
        private principal: Principal,
        private eventManager: EventManager,
        private sidebarService: SidebarService,
        private router: Router,
        private modalService: NgbModal,
        private modeloExclusivoService: ModeloExclusivoService,
        private shareService: ShareService,
        private customizeService: CustomizeService,
        private baseService: BaseService,
        private modeloService: ModeloService,
        private accountService: AccountService,
        private cenarioService: CenarioService,
    ) {
        this.isSidebarFixed = sidebarService.isLock();

        sidebarService.lockedObserver$.subscribe((lock: boolean) => {
            this.isSidebarFixed = lock;
        });

        this.eventManager.subscribe('customizeListModification', () => {
            this.customizeService.getCustomize()
                .subscribe(
                    (customize: Customize) => {
                        if (customize) {
                            this.projeto = customize.projeto;
                            this.cenario = customize.cenario;
                            this.customize = customize;
                        }else{
                            this.projeto =  null;
                            this.cenario =  null;
                            this.customize = null;
                        }
                        this.closeMenuBases();
                        this.closeMenuModelos();
                    }
                );
        });
    }

    ngOnInit() {
        this.principal.identity().then((account: Account) => {
            this.getDados(account);
        });
        this.principal.getAuthenticationState().subscribe((account) => {
            this.getDados(account);
        });
        this.registerAuthenticationSuccess();
        this.openSidebar();
    }

    private registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', () => {
            this.principal.identity().then((account: Account) => {
                this.getDados(account);
                this.openSidebar();
            });
        });
    }

    private getDados(account: Account) {
        if (!account) {
            this.closeSidebar();
        } else {
            this.nome =
                (account.firstName ? account.firstName : '') + ' ' +
                (account.lastName ? account.lastName : '');
            this.email = account.email;
            this.accountService.getEndereco()
                .subscribe(
                    (endereco: string) => {
                        console.log('endereco do servidor:  '+ endereco);
                        this.image = account.imageUrl ? (endereco + account.imageUrl) : null;
                    }
                );
        }
    }

    private toogleBlockSideBar() {
        this.sidebarService.toogleSidebarFixed();
        if (!this.sidebarService.isLock()) {
            this.closeSidebar();
        }
    }

    private openSidebar() {
        this.sidebarService.openSidebar();
    }

    private closeSidebar() {
        this.sidebarService.closeSidebar();
    }

    private isUserAuthenticated() {
        return this.principal.isAuthenticated();
    }

    private configurar() {
        if (!this.nome || this.nome === ' ' || !this.image) {
            this.router.navigate(['/settings']);
        }
    }

    closeMenuModelos() {
        this.isModelosOpen = false;
        this.modelosMapeados = [];
        this.modelos = null;
    }

    closeMenuBases() {
        this.isbasesOpen = false;
        this.bases = null;
    }

    trackId(index: number, item: any) {
        return item.id;
    }

    isModeloMapeado(modelo: Modelo): boolean {
        return (this.modelosMapeados.indexOf(modelo.id) > -1);
    }

    mapear(modelo: Modelo) {
        if (!this.isModeloMapeado(modelo) && this.cenario) {
            let ref: NgbModalRef;
            ref = this.modalService.open(MapearModeloComponent);
            ref.componentInstance.setModelo(modelo);
            ref.componentInstance.setCenario(this.cenario);
            ref.componentInstance.setProjeto(this.projeto);
        }
    }

    excluirMapeamento(modelo: Modelo) {
        this.modeloExclusivoService.getModeloExclusivoByModeloAndCenario(modelo, this.cenario)
            .subscribe(
                (modelosExclusivos: ModeloExclusivo[]) => {
                    modelosExclusivos.forEach((modeloEx: ModeloExclusivo) => {
                        this.router.navigate(['/', { outlets: { popup: 'modelo-exclusivo/'+ modeloEx.id + '/delete'} }]);
                        this.closeMenuModelos();
                    });
                }
            );
    }

    addNewBase() {
        this.customizeService.getCustomize()
            .subscribe(
                (customize: Customize) => {
                    if( customize && customize.projeto) {
                        this.modalService.open(FabAddBaseComponent);
                    }
                }
            );
    }

    addNewModelo() {
        this.modalService.open(FabAddModeloComponent);
    }

    loadAllBases() {
        this.closeMenuModelos();
        this.closeMenuArquivos();
        if (this.bases === null) {
            this.loadBases = true;
            this.customizeService.getCustomize()
                .subscribe(
                    (customize: Customize) => {
                        if (customize && customize.projeto) {
                            this.baseService
                                .getBasesByProjeto(customize.projeto)
                                .subscribe(
                                    (bases: Base[]) => {
                                        this.bases = bases;
                                        this.isbasesOpen = true;
                                    },
                                    () => {
                                        this.bases = null;
                                        this.isbasesOpen = false;
                                    });
                        }
                    },
                    () => {

                    },
                    () => {
                        this.loadBases = false;
                    });

        } else {
            this.bases = null;
            this.isbasesOpen = false;
        }
    }

    loadAllModelos() {
        this.closeMenuBases();
        this.closeMenuArquivos();
        if (this.modelos === null) {
            this.loadModelos = true;
            this.modeloService.getAllModelos().subscribe(
                (modelos: Modelo[]) => {
                    this.modelos = modelos;
                    this.isModelosOpen = true;
                    modelos.forEach((modelo) => {
                        this.modeloExclusivoService
                            .getModeloExclusivoByModeloAndCenario(modelo, this.cenario)
                            .subscribe((modelosExclusivos: ModeloExclusivo[]) => {
                                if (
                                    modelosExclusivos !== null &&
                                    modelosExclusivos !== undefined &&
                                    modelosExclusivos.length > 0) {
                                    this.modelosMapeados.push(modelo.id);
                                }
                            });
                    });
                },
                () => {

                },
                () => {
                    this.loadModelos = false;
                }
            );
        } else {
            this.isModelosOpen = false;
            this.modelos = null;
        }
    }

    private selecionarProjeto() {
        this.modalService.open(SelecionarProjetoComponent);
    }

    private selecionarCenario() {
        this.customizeService.getCustomize()
            .subscribe(
                (customize: Customize) => {
                    if (customize && customize.projeto) {
                        this.modalService.open(SelecionarCenarioComponent);
                    }
                });
    }

    compartilharBase(base: Base){
        this.shareService.compartilharBase(base);
    }

    compartilharModelo(modelo: Modelo){
        this.shareService.compartilharModelo(modelo);
    }

    listar() {

        if(this.isArquivosOpen){
            this.closeMenuArquivos();
        } else {
            this.loadArquivos = true;
            this.closeMenuBases();
            this.closeMenuModelos();
            this.isArquivosOpen = true;
            this.cenarioService
                .listFiles(this.cenario)
                .subscribe(
                    (res: string) => {
                        res.split(',').forEach((arq) => {
                            this.arquivos.push(
                                arq.substring(
                                this.cenario.caminho.length
                                )
                            );
                        });
                    },
                    () => {
                        alert('Houve um erro ao obter a lista de arquivos!');
                        this.closeMenuArquivos();
                    },
                    () => {
                        this.loadArquivos = false;
                    }
                );
        }
    }

    closeMenuArquivos() {
        this.isArquivosOpen = false;
        this.arquivos = [];
    }

}

