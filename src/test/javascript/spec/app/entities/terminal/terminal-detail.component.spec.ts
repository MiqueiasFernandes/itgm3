import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { ItgmTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TerminalDetailComponent } from '../../../../../../main/webapp/app/entities/terminal/terminal-detail.component';
import { TerminalService } from '../../../../../../main/webapp/app/entities/terminal/terminal.service';
import { Terminal } from '../../../../../../main/webapp/app/entities/terminal/terminal.model';

describe('Component Tests', () => {

    describe('Terminal Management Detail Component', () => {
        let comp: TerminalDetailComponent;
        let fixture: ComponentFixture<TerminalDetailComponent>;
        let service: TerminalService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ItgmTestModule],
                declarations: [TerminalDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TerminalService,
                    EventManager
                ]
            }).overrideComponent(TerminalDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TerminalDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TerminalService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Terminal(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.terminal).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
