import { Component } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {SidebarService} from '../sidebar/sidebar.service';

import {
    FabAddProjetoComponent,
    FabAddCenarioComponent,
    FabAddBaseComponent,
    FabAddModeloComponent,
    FabAddTerminalComponent,
    FabAddPrognoseComponent,
} from '../../entities';

import {CustomizeService} from "../../entities/customize/customize.service";
import {Customize} from "../../entities/customize/customize.model";

@Component({
    selector: 'jhi-fab-add',
    templateUrl: './fab-add.component.html',
    styleUrls: [
        'fab-add.scss'
    ],
})
export class FabAddComponent {

    menuOpen = false;
    aberto = false;

    constructor(
        private sidebarService: SidebarService,
        private modalService: NgbModal,
        private customizeService: CustomizeService
    ) {
        sidebarService.sidebarObserver$.subscribe((open: boolean) => {
            this.aberto = open;
            this.menuOpen = false;
        });
    }

    toogleMenu() {
        this.menuOpen = !this.menuOpen;
    }

    addNewProjeto() {
        this.modalService.open(FabAddProjetoComponent);
    }

    addNewCenario() {
        this.customizeService.getCustomize()
            .subscribe(
                (customize: Customize) => {
                    if(customize && customize.projeto) {
                        this.modalService.open(FabAddCenarioComponent);
                    }
                }
            );
    }

    addNewBase() {
        this.customizeService.getCustomize()
            .subscribe(
                (customize: Customize) => {
                    if(customize && customize.projeto) {
                        this.modalService.open(FabAddBaseComponent);
                    }
                }
            );
    }

    addNewModelo() {
        this.modalService.open(FabAddModeloComponent);
    }

    openTerminal() {
        this.modalService.open(FabAddTerminalComponent);
    }

    novaPrognose() {
        this.modalService.open(FabAddPrognoseComponent);
    }
}
