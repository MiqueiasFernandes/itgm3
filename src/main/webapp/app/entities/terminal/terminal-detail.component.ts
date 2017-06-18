import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager , JhiLanguageService  } from 'ng-jhipster';

import { Terminal } from './terminal.model';
import { TerminalService } from './terminal.service';

@Component({
    selector: 'jhi-terminal-detail',
    templateUrl: './terminal-detail.component.html'
})
export class TerminalDetailComponent implements OnInit, OnDestroy {

    terminal: Terminal;
    private subscription: any;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private jhiLanguageService: JhiLanguageService,
        private terminalService: TerminalService,
        private route: ActivatedRoute
    ) {
        this.jhiLanguageService.setLocations(['terminal']);
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTerminals();
    }

    load(id) {
        this.terminalService.find(id).subscribe((terminal) => {
            this.terminal = terminal;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTerminals() {
        this.eventSubscriber = this.eventManager.subscribe('terminalListModification', (response) => this.load(this.terminal.id));
    }
}
